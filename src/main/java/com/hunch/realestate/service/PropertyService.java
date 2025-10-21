package com.hunch.realestate.service;

import com.hunch.realestate.common.enums.PropertyType;
import com.hunch.realestate.domain.dto.PagingResult;
import com.hunch.realestate.domain.dto.PropertyDTO;
import com.hunch.realestate.domain.entity.Property;
import com.hunch.realestate.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropertyService {
    private static final String IMAGES_BASE_PATH = "data/images";

    private final PropertyRepository propertyRepository;

    /**
     * 전체 매물 개수 조회
     */
    public long getTotalCount() {
        return propertyRepository.count();
    }

    /**
     * 매물 목록 조회 (페이징)
     */
    public PagingResult<PropertyDTO> getProperties(PropertyType type, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Property> propertyPage;

        if (type != null) {
            propertyPage = propertyRepository.findByPropertyType(type, pageable);
        } else {
            propertyPage = propertyRepository.findAll(pageable);
        }

        List<PropertyDTO> content = propertyPage.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PagingResult<>(
                content,
                page,
                size,
                (int) propertyPage.getTotalElements(),
                propertyPage.getTotalPages()
        );
    }

    /**
     * 매물 상세 조회
     */
    public PropertyDTO getProperty(String id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("매물을 찾을 수 없습니다: " + id));
        return convertToDTO(property);
    }

    /**
     * 매물 등록
     */
    public void register(PropertyDTO propertyDTO, List<MultipartFile> images) {
        Property property = convertToEntity(propertyDTO);

        // 먼저 엔티티를 저장하여 ID를 생성
        property = propertyRepository.save(property);

        // 이미지 처리
        if (images != null && !images.isEmpty()) {
            List<String> imagePaths = handleImageUpload(property.getId(), images);
            property.setPhotoUrls(String.join(",", imagePaths));
            propertyRepository.save(property);
        }
    }

    /**
     * 매물 수정
     */
    public void update(String id, PropertyDTO propertyDTO, List<MultipartFile> newImages) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("매물을 찾을 수 없습니다: " + id));

        // 기존 속성 업데이트
        updatePropertyFields(property, propertyDTO);

        // 새 이미지 추가
        if (newImages != null && !newImages.isEmpty()) {
            List<String> newImagePaths = handleImageUpload(property.getId(), newImages);

            // 기존 이미지와 새 이미지 병합
            List<String> currentImages = new ArrayList<>();
            if (property.getPhotoUrls() != null && !property.getPhotoUrls().isEmpty()) {
                currentImages.addAll(Arrays.asList(property.getPhotoUrls().split(",")));
            }
            currentImages.addAll(newImagePaths);
            property.setPhotoUrls(String.join(",", currentImages));
        }

        propertyRepository.save(property);
    }

    /**
     * 매물 삭제
     */
    public void delete(String id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("매물을 찾을 수 없습니다: " + id));

        // 이미지 파일 삭제
        deletePropertyImages(property);

        propertyRepository.delete(property);
    }

    /**
     * 이미지 삭제
     */
    public void deleteImage(String propertyId, String filename) {
        try {
            Path imagePath = Paths.get(IMAGES_BASE_PATH, propertyId, filename);
            Files.deleteIfExists(imagePath);

            // 이미지 목록에서도 제거
            Property property = propertyRepository.findById(propertyId)
                    .orElseThrow(() -> new RuntimeException("매물을 찾을 수 없습니다: " + propertyId));

            if (property.getPhotoUrls() != null) {
                List<String> imageList = new ArrayList<>(Arrays.asList(property.getPhotoUrls().split(",")));
                imageList.remove(propertyId + "/" + filename);
                property.setPhotoUrls(String.join(",", imageList));
                propertyRepository.save(property);
            }
        } catch (IOException e) {
            log.error("이미지 삭제 중 오류 발생: ", e);
            throw new RuntimeException("이미지 삭제 실패");
        }
    }

    /**
     * Private helper methods
     */

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
        if (property.getPhotoUrls() != null && !property.getPhotoUrls().isEmpty()) {
            String[] imagePaths = property.getPhotoUrls().split(",");
            for (String imagePath : imagePaths) {
                try {
                    Files.deleteIfExists(Paths.get(IMAGES_BASE_PATH, imagePath.trim()));
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
        dto.setId(property.getId());
        dto.setPropertyType(property.getPropertyType());
        dto.setAddress(property.getAddress());
        dto.setDetailAddress(property.getDetailAddress());
        dto.setLatitude(property.getLatitude());
        dto.setLongitude(property.getLongitude());
        dto.setTransactionType(property.getTransactionType());
        dto.setPrice(property.getPrice());
        dto.setDeposit(property.getDeposit());
        dto.setMonthlyRent(property.getMonthlyRent());

        // 이미지 URL 문자열을 리스트로 변환
        if (property.getPhotoUrls() != null && !property.getPhotoUrls().isEmpty()) {
            dto.setImages(Arrays.asList(property.getPhotoUrls().split(",")));
        }

        dto.setDescription(property.getDescription());
        dto.setCreatedAt(property.getCreatedAt());
        dto.setUpdatedAt(property.getUpdatedAt());
        return dto;
    }

    private Property convertToEntity(PropertyDTO dto) {
        Property property = new Property();
        if (dto.getId() != null) {
            property.setId(dto.getId());
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

        // 이미지 리스트를 콤마로 구분된 문자열로 변환
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            property.setPhotoUrls(String.join(",", dto.getImages()));
        }

        property.setDescription(dto.getDescription());
        return property;
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