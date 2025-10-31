package com.hunch.realestate.web.controller.api;

import com.hunch.realestate.common.enums.PropertyType;
import com.hunch.realestate.domain.dto.PagingResult;
import com.hunch.realestate.domain.dto.PropertyDTO;
import com.hunch.realestate.service.PropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 매물 관리 REST API 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/admin/properties")
@RequiredArgsConstructor
public class PropertyResource {

    private final PropertyService propertyService;

    /**
     * 매물 목록 조회 (API)
     */
    @GetMapping
    public ResponseEntity<PagingResult<PropertyDTO>> getProperties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) PropertyType type) {
        try {
            PagingResult<PropertyDTO> result = propertyService.getProperties(type, page, size);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("매물 목록 조회 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 매물 상세 조회 (API)
     */
    @GetMapping("/{id}")
    public ResponseEntity<PropertyDTO> getProperty(@PathVariable String id) {
        try {
            PropertyDTO property = propertyService.getProperty(id);
            return ResponseEntity.ok(property);
        } catch (Exception e) {
            log.error("매물 조회 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 매물 등록
     */
    @PostMapping
    public ResponseEntity<?> registerProperty(
            @ModelAttribute PropertyDTO propertyDTO,
            @RequestParam(required = false) List<MultipartFile> images) {

        Map<String, String> errors = propertyDTO.validate();
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            propertyService.register(propertyDTO, images);
            return ResponseEntity.ok().body("매물이 등록되었습니다.");
        } catch (Exception e) {
            log.error("매물 등록 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("매물 등록 중 오류가 발생했습니다.");
        }
    }

    /**
     * 매물 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProperty(
            @PathVariable String id,
            @ModelAttribute PropertyDTO propertyDTO,
            @RequestParam(required = false) List<MultipartFile> images) {

        Map<String, String> errors = propertyDTO.validate();
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            propertyService.update(id, propertyDTO, images);
            return ResponseEntity.ok().body("매물이 수정되었습니다.");
        } catch (Exception e) {
            log.error("매물 수정 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("매물 수정 중 오류가 발생했습니다.");
        }
    }

    /**
     * 매물 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProperty(@PathVariable String id) {
        try {
            propertyService.delete(id);
            return ResponseEntity.ok().body("매물이 삭제되었습니다.");
        } catch (Exception e) {
            log.error("매물 삭제 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("매물 삭제 중 오류가 발생했습니다.");
        }
    }

    /**
     * 이미지 삭제
     */
    @DeleteMapping("/images/{propertyId}/{filename}")
    public ResponseEntity<String> deleteImage(
            @PathVariable String propertyId,
            @PathVariable String filename) {
        try {
            propertyService.deleteImage(propertyId, filename);
            return ResponseEntity.ok("이미지가 삭제되었습니다.");
        } catch (Exception e) {
            log.error("이미지 삭제 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 삭제 중 오류가 발생했습니다.");
        }
    }

    /**
     * 통계 - 전체 매물 개수
     */
    @GetMapping("/statistics/total-count")
    public ResponseEntity<Map<String, Long>> getTotalCount() {
        try {
            long totalCount = propertyService.getTotalCount();
            return ResponseEntity.ok(Map.of("totalCount", totalCount));
        } catch (Exception e) {
            log.error("매물 통계 조회 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
