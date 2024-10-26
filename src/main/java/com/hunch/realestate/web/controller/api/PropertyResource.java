package com.hunch.realestate.web.controller.api;

import com.hunch.realestate.common.enums.PropertyType;
import com.hunch.realestate.domain.dto.PropertyDTO;
import com.hunch.realestate.service.PropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PropertyResource {

    private final PropertyService propertyService;

    /**
     * 매물 등록
     */
    @PostMapping("/api/v1/admin/properties")
    public ResponseEntity<?> registerProperty(
            @ModelAttribute PropertyDTO propertyDTO,
            @RequestParam(required = false) List<MultipartFile> images) {

        Map<String, String> errors = propertyDTO.validate();
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            propertyService.register(propertyDTO, images);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("매물 등록 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("매물 등록 중 오류가 발생했습니다.");
        }
    }

    /**
     * 매물 수정
     */
    @PutMapping("/api/v1/admin/properties/{id}")
    public ResponseEntity<?> updateProperty(
            @PathVariable Long id,
            @ModelAttribute PropertyDTO propertyDTO,
            @RequestParam(required = false) List<MultipartFile> images) {

        Map<String, String> errors = propertyDTO.validate();
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            propertyService.update(id, propertyDTO, images);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("매물 수정 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("매물 수정 중 오류가 발생했습니다.");
        }
    }

    /**
     * 매물 삭제
     */
    @DeleteMapping("/api/v1/admin/properties/{id}")
    public ResponseEntity<String> deleteProperty(@PathVariable Long id) {
        try {
            propertyService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("매물 삭제 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("매물 삭제 중 오류가 발생했습니다.");
        }
    }

    /**
     * 이미지 삭제
     */
    @DeleteMapping("/api/v1/admin/properties/images/{propertyId}/{filename}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable String propertyId,
            @PathVariable String filename) {
        try {
            propertyService.deleteImage(propertyId, filename);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("이미지 삭제 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 매물 타입별 추가 필드 조회
     */
    @GetMapping("/api/v1/admin/properties/property-type-fields")
    public ResponseEntity<Map<String, List<String>>> getPropertyTypeFields(
            @RequestParam String type) {
        Map<String, List<String>> fields = new HashMap<>();

        PropertyType propertyType = PropertyType.valueOf(type);
        switch (propertyType) {
            case APARTMENT:
                fields.put("fields", List.of("평수", "층수", "방향", "현관구조"));
                break;
            case VILLA:
                fields.put("fields", List.of("평수", "층수", "방향", "주차가능여부"));
                break;
            case ONE_ROOM, TWOTHREE_ROOM:
                fields.put("fields", List.of("평수", "층수", "방향", "복층여부"));
                break;
            case COMMERCIAL:
                fields.put("fields", List.of("평수", "층수", "엘리베이터", "주차가능여부"));
                break;
            case STORE:
                fields.put("fields", List.of("평수", "층수", "업종제한"));
                break;
            default:
                fields.put("fields", new ArrayList<>());
        }

        return ResponseEntity.ok(fields);
    }
}