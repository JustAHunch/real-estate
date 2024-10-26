package com.hunch.realestate.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hunch.realestate.common.enums.PropertyType;
import com.hunch.realestate.domain.dto.PagingResult;
import com.hunch.realestate.domain.*;
import com.hunch.realestate.domain.dto.PropertyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropertyService {
    private final JsonStorageService storageService;
    private static final String PROPERTIES_JSON_PATH = "data/properties.json";
    private static final String IMAGES_BASE_PATH = "data/images";

    /**
     * 매물 목록 조회 (페이징)
     */
    public PagingResult<PropertyDTO> getProperties(PropertyType type, int page, int size) {
        List<Property> properties = getAllProperties();

        // 타입 필터링
        if (type != null) {
            properties = properties.stream()
                    .filter(p -> p.getPropertyType() == type)
                    .collect(Collectors.toList());
        }

        // 정렬 (최신순)
        properties.sort((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));

        // 페이징 처리
        int totalCount = properties.size();
        int totalPages = (int) Math.ceil((double) totalCount / size);
        int start = page * size;
        int end = Math.min(start + size, totalCount);

        // 범위 체크
        if (start >= totalCount) {
            return new PagingResult<>(
                    new ArrayList<>(),
                    page,
                    size,
                    totalCount,
                    totalPages
            );
        }

        List<PropertyDTO> content = properties.subList(start, end)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PagingResult<>(
                content,
                page,
                size,
                totalCount,
                totalPages
        );
    }

    /**
     * 매물 상세 조회
     */
    public PropertyDTO getProperty(Long id) {
        Property property = findPropertyById(id);
        return convertToDTO(property);
    }

    /**
     * 매물 등록
     */
    public void register(PropertyDTO propertyDTO, List<MultipartFile> images) {
        Property property = convertToEntity(propertyDTO);
        property.setId(UUID.randomUUID().toString());
        property.setCreatedAt(LocalDateTime.now());
        property.setUpdatedAt(LocalDateTime.now());

        // 이미지 처리
        if (images != null && !images.isEmpty()) {
            List<String> imagePaths = handleImageUpload(property.getId(), images);
            property.setImages(imagePaths);
        }

        List<Property> properties = getAllProperties();
        properties.add(property);
        saveProperties(properties);
    }

    /**
     * 매물 수정
     */
    public void update(Long id, PropertyDTO propertyDTO, List<MultipartFile> newImages) {
        List<Property> properties = getAllProperties();
        Property property = findPropertyById(id);

        // 기존 속성 업데이트
        updatePropertyFields(property, propertyDTO);
        property.setUpdatedAt(LocalDateTime.now());

        // 새 이미지 추가
        if (newImages != null && !newImages.isEmpty()) {
            List<String> newImagePaths = handleImageUpload(property.getId(), newImages);
            List<String> currentImages = property.getImages() != null ? property.getImages() : new ArrayList<>();
            currentImages.addAll(newImagePaths);
            property.setImages(currentImages);
        }

        saveProperties(properties);
    }

    /**
     * 매물 삭제
     */
    public void delete(Long id) {
        List<Property> properties = getAllProperties();
        Property property = findPropertyById(id);

        // 이미지 파일 삭제
        deletePropertyImages(property);

        properties.removeIf(p -> p.getId().equals(property.getId()));
        saveProperties(properties);
    }

    /**
     * 이미지 삭제
     */
    public void deleteImage(String propertyId, String filename) {
        try {
            Path imagePath = Paths.get(IMAGES_BASE_PATH, propertyId, filename);
            Files.deleteIfExists(imagePath);

            // 이미지 목록에서도 제거
            Property property = findPropertyById(Long.parseLong(propertyId));
            if (property.getImages() != null) {
                property.getImages().remove(propertyId + "/" + filename);
                List<Property> properties = getAllProperties();
                saveProperties(properties);
            }
        } catch (IOException e) {
            log.error("이미지 삭제 중 오류 발생: ", e);
            throw new RuntimeException("이미지 삭제 실패");
        }
    }

    /**
     * Private helper methods
     */
    private List<Property> getAllProperties() {
        return storageService.readList(PROPERTIES_JSON_PATH, new TypeReference<List<Property>>() {});
    }

    private void saveProperties(List<Property> properties) {
        storageService.writeList(PROPERTIES_JSON_PATH, properties);
    }

    private Property findPropertyById(Long id) {
        return getAllProperties().stream()
                .filter(p -> p.getId().equals(id.toString()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("매물을 찾을 수 없습니다: " + id));
    }

    private List<String> handleImageUpload(String propertyId, List<MultipartFile> images) {
        List<String> imagePaths = new ArrayList<>();
        Path propertyImageDir = Paths.get(IMAGES_BASE_PATH, propertyId);

        try {
            Files.createDirectories(propertyImageDir);

            for (MultipartFile image : images) {
                String originalFilename = image.getOriginalFilename();
                String newFilename = UUID.randomUUID().toString() +
                        getFileExtension(originalFilename);
                Path imagePath = propertyImageDir.resolve(newFilename);

                image.transferTo(imagePath.toFile());
                imagePaths.add(propertyId + "/" + newFilename);
            }
        } catch (IOException e) {
            log.error("이미지 업로드 중 오류 발생: ", e);
            throw new RuntimeException("이미지 업로드 실패");
        }

        return imagePaths;
    }

    private void deletePropertyImages(Property property) {
        if (property.getImages() != null) {
            for (String imagePath : property.getImages()) {
                try {
                    Files.deleteIfExists(Paths.get(IMAGES_BASE_PATH, imagePath));
                } catch (IOException e) {
                    log.error("이미지 삭제 중 오류 발생: ", e);
                }
            }
        }

        // 매물 이미지 디렉토리 삭제
        try {
            Files.deleteIfExists(Paths.get(IMAGES_BASE_PATH, property.getId()));
        } catch (IOException e) {
            log.error("이미지 디렉토리 삭제 중 오류 발생: ", e);
        }
    }

    private String getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(f.lastIndexOf(".")))
                .orElse(".jpg");
    }

    private PropertyDTO convertToDTO(Property property) {
        PropertyDTO dto = new PropertyDTO();
        dto.setId(Long.parseLong(property.getId()));
        dto.setPropertyType(property.getPropertyType());
        dto.setAddress(property.getAddress());
        dto.setDetailAddress(property.getDetailAddress());
        dto.setLatitude(property.getLatitude());
        dto.setLongitude(property.getLongitude());
        dto.setTransactionType(property.getTransactionType());
        dto.setPrice(property.getPrice());
        dto.setDeposit(property.getDeposit());
        dto.setMonthlyRent(property.getMonthlyRent());
        dto.setImages(property.getImages());
        dto.setDescription(property.getDescription());
        dto.setCreatedAt(property.getCreatedAt());
        dto.setUpdatedAt(property.getUpdatedAt());
        return dto;
    }

    private Property convertToEntity(PropertyDTO dto) {
        Property property = createPropertyInstance(dto.getPropertyType());
        if (dto.getId() != null) {
            property.setId(dto.getId().toString());
        }
        property.setPropertyType(dto.getPropertyType());
        property.setAddress(dto.getAddress());
        property.setDetailAddress(dto.getDetailAddress());
        property.setLatitude(dto.getLatitude());
        property.setLongitude(dto.getLongitude());
        property.setTransactionType(dto.getTransactionType());
        property.setPrice(dto.getPrice());
        property.setDeposit(dto.getDeposit());
        property.setMonthlyRent(dto.getMonthlyRent());
        property.setImages(dto.getImages());
        property.setDescription(dto.getDescription());
        return property;
    }

    private Property createPropertyInstance(PropertyType type) {
        switch (type) {
            case APARTMENT:
                return new ApartmentProperty();
            case VILLA:
                return new VillaProperty();
            case ONE_ROOM:
                return new OneRoomProperty();
            case COMMERCIAL:
                return new CommercialProperty();
            case STORE:
                return new StoreProperty();
            default:
                throw new IllegalArgumentException("지원하지 않는 매물 타입입니다: " + type);
        }
    }

    private void updatePropertyFields(Property property, PropertyDTO dto) {
        property.setPropertyType(dto.getPropertyType());
        property.setAddress(dto.getAddress());
        property.setDetailAddress(dto.getDetailAddress());
        property.setLatitude(dto.getLatitude());
        property.setLongitude(dto.getLongitude());
        property.setTransactionType(dto.getTransactionType());
        property.setPrice(dto.getPrice());
        property.setDeposit(dto.getDeposit());
        property.setMonthlyRent(dto.getMonthlyRent());
        property.setDescription(dto.getDescription());
    }
}