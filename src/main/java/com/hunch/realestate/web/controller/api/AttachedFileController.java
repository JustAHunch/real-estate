package com.hunch.realestate.web.controller.api;

import com.hunch.realestate.config.CurrentUser;
import com.hunch.realestate.domain.dto.AttachedFileDTO;
import com.hunch.realestate.domain.entity.User;
import com.hunch.realestate.service.AttachedFileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 첨부파일 API Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class AttachedFileController {

    private final AttachedFileService fileService;

    /**
     * 파일 목록 조회
     * GET /api/v1/files?fileGrpId=xxx
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getFileList(
            @RequestParam String fileGrpId) {
        try {
            List<AttachedFileDTO> files = fileService.getFileList(fileGrpId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", files);
            response.put("count", files.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("파일 목록 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * 단일 파일 조회
     * GET /api/v1/files/{fileUid}
     */
    @GetMapping("/{fileUid}")
    public ResponseEntity<Map<String, Object>> getFile(@PathVariable String fileUid) {
        try {
            AttachedFileDTO file = fileService.getFile(fileUid);
            return ResponseEntity.ok(Map.of("success", true, "data", file));
        } catch (Exception e) {
            log.error("파일 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * 파일 업로드
     * POST /api/v1/files/upload
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(required = false) String fileGrpId,
            @RequestParam(required = false) String propertyName,
            @RequestParam(required = false) String propertyUid,
            @CurrentUser User currentUser) {
        try {
            // 현재 로그인한 사용자 ID 가져오기
            String userId = currentUser.getUsername();

            log.info("파일 업로드 요청 - 사용자: {}, 파일 수: {}, 매물명: {}, 매물UID: {}",
                    userId, files.size(), propertyName, propertyUid);

            String resultFileGrpId = fileService.uploadFiles(files, userId, fileGrpId, propertyName, propertyUid);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("fileGrpId", resultFileGrpId);
            response.put("message", files.size() + "개 파일이 업로드되었습니다.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("파일 업로드 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * 파일 다운로드
     * GET /api/v1/files/download/{fileUid}
     */
    @GetMapping("/download/{fileUid}")
    public void downloadFile(
            @PathVariable String fileUid,
            HttpServletResponse response) {
        try {
            fileService.downloadFile(fileUid, response);
        } catch (Exception e) {
            log.error("파일 다운로드 실패: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 여러 파일 ZIP 다운로드
     * GET /api/v1/files/download-zip?fileUids=uid1,uid2,uid3
     */
    @GetMapping("/download-zip")
    public void downloadFilesAsZip(
            @RequestParam String fileUids,
            HttpServletResponse response) {
        try {
            List<String> fileUidList = Arrays.asList(fileUids.split(","));
            fileService.downloadFilesAsZip(fileUidList, response);
        } catch (Exception e) {
            log.error("ZIP 파일 다운로드 실패: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 파일 삭제 (Soft Delete)
     * DELETE /api/v1/files/{fileUid}
     */
    @DeleteMapping("/{fileUid}")
    public ResponseEntity<Map<String, Object>> deleteFile(@PathVariable String fileUid) {
        try {
            fileService.deleteFile(fileUid);
            return ResponseEntity.ok(Map.of("success", true, "message", "파일이 삭제되었습니다."));
        } catch (Exception e) {
            log.error("파일 삭제 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * 여러 파일 삭제
     * DELETE /api/v1/files/batch
     */
    @DeleteMapping("/batch")
    public ResponseEntity<Map<String, Object>> deleteFiles(
            @RequestBody Map<String, List<String>> request) {
        try {
            List<String> fileUids = request.get("fileUids");
            if (fileUids == null || fileUids.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "삭제할 파일을 선택해주세요."));
            }

            fileService.deleteFiles(fileUids);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", fileUids.size() + "개 파일이 삭제되었습니다."));
        } catch (Exception e) {
            log.error("파일 일괄 삭제 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * 에디터 이미지 업로드
     * POST /api/v1/files/editor-image
     */
    @PostMapping("/editor-image")
    public ResponseEntity<Map<String, Object>> uploadEditorImage(
            @RequestParam("upload") MultipartFile file,
            @CurrentUser User currentUser) {
        try {
            // 현재 로그인한 사용자 ID 가져오기
            String userId = (currentUser != null && currentUser.getUsername() != null)
                    ? currentUser.getUsername()
                    : "system";

            log.info("에디터 이미지 업로드 - 사용자: {}, 파일명: {}", userId, file.getOriginalFilename());

            AttachedFileDTO uploadedFile = fileService.uploadEditorImage(file, userId);

            Map<String, Object> response = new HashMap<>();
            response.put("uploaded", 1);
            response.put("fileName", uploadedFile.getFileNm());
            response.put("url", "/editorimg/" + uploadedFile.getFileNm());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("에디터 이미지 업로드 실패: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("uploaded", 0);
            errorResponse.put("error", Map.of("message", e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 파일 그룹 ID 생성
     * GET /api/v1/files/generate-file-grp-id
     */
    @GetMapping("/generate-file-grp-id")
    public ResponseEntity<Map<String, Object>> generateFileGrpId() {
        try {
            String fileGrpId = fileService.generateFileGrpId();
            return ResponseEntity.ok(Map.of("success", true, "fileGrpId", fileGrpId));
        } catch (Exception e) {
            log.error("파일 그룹 ID 생성 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}