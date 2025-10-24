package com.hunch.realestate.service;

import com.hunch.realestate.domain.dto.AttachedFileDTO;
import com.hunch.realestate.domain.entity.AttachedFile;
import com.hunch.realestate.repository.AttachedFileRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 첨부파일 Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AttachedFileService {

    private final AttachedFileRepository fileRepository;

    @Value("${app.storage.upload-path}")
    private String uploadPath;

    @Value("${app.storage.temp-path}")
    private String tempPath;

    @Value("${app.storage.attfile-path}")
    private String attfilePath;

    @Value("${app.storage.editorimg-path}")
    private String editorimgPath;

    @Value("${app.storage.block-ext}")
    private String blockExt;

    @Value("${app.storage.maxfileSize:52428800}") // 50MB default
    private long maxFileSize;

    /**
     * 파일 그룹 ID 생성
     */
    public String generateFileGrpId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());
        String uuid = UUID.randomUUID().toString().substring(0, 5);
        return date + uuid.toUpperCase();
    }

    /**
     * 파일 UID 생성
     */
    private String generateFileUid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 파일 목록 조회 (파일 그룹 ID로)
     */
    @Transactional(readOnly = true)
    public List<AttachedFileDTO> getFileList(String fileGrpId) {
        List<AttachedFile> files = fileRepository.findByFileGrpId(fileGrpId);
        return files.stream()
                .map(AttachedFileDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 단일 파일 조회
     */
    @Transactional(readOnly = true)
    public AttachedFileDTO getFile(String fileUid) {
        AttachedFile file = fileRepository.findByFileUidAndDelYn(fileUid, false)
                .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다: " + fileUid));
        return AttachedFileDTO.fromEntity(file);
    }

    /**
     * 파일 업로드
     */
    @Transactional
    public String uploadFiles(List<MultipartFile> files, String userId, String fileGrpId, String propertyName, String propertyUid)
            throws IOException {

        // 파일 그룹 ID가 없으면 생성
        if (fileGrpId == null || fileGrpId.isEmpty()) {
            fileGrpId = generateFileGrpId();
        }

        // 파일 저장 경로 설정
        String basePath = attfilePath;
        File baseDir = new File(basePath);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }

        // 월별 폴더 생성
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String monthFolder = sdf.format(new Date());
        String filePath = basePath + "/" + monthFolder;

        // 매물명_매물UID 폴더 추가 (제공된 경우)
        if (propertyName != null && !propertyName.isEmpty() && propertyUid != null && !propertyUid.isEmpty()) {
            filePath = filePath + "/" + propertyName + "_" + propertyUid;
        }
        File monthDir = new File(filePath);
        if (!monthDir.exists()) {
            monthDir.mkdirs();
        }

        // 각 파일 처리
        for (MultipartFile multipartFile : files) {
            if (multipartFile.isEmpty()) {
                continue;
            }

            // 파일 크기 체크
            if (multipartFile.getSize() > maxFileSize) {
                throw new IOException("파일 크기가 최대 크기를 초과합니다: " + multipartFile.getOriginalFilename());
            }

            String originalFileName = multipartFile.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

            // 차단된 확장자 체크
            if (blockExt.toLowerCase().contains(fileExtension.toLowerCase())) {
                throw new IOException("업로드할 수 없는 파일 형식입니다: " + fileExtension);
            }

            // 파일 UID 생성
            String fileUid = generateFileUid();
            String storedFileName = fileUid + fileExtension;

            // 파일 저장
            File destFile = new File(filePath, storedFileName);
            multipartFile.transferTo(destFile);

            // DB에 파일 정보 저장
            AttachedFile attachedFile = AttachedFile.builder()
                    .fileGrpId(fileGrpId)
                    .fileUid(fileUid)
                    .fileOriginNm(originalFileName)
                    .fileType(fileExtension)
                    .filePath(filePath)
                    .fileNm(storedFileName)
                    .fileSize(multipartFile.getSize())
                    .delYn(false)
                    .inputUserId(userId)
                    .build();

            fileRepository.save(attachedFile);
            log.info("파일 업로드 완료: {} -> {}", originalFileName, storedFileName);
        }

        return fileGrpId;
    }

    /**
     * 파일 다운로드
     */
    @Transactional(readOnly = true)
    public void downloadFile(String fileUid, HttpServletResponse response) throws IOException {
        AttachedFile file = fileRepository.findByFileUidAndDelYn(fileUid, false)
                .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다: " + fileUid));

        File downloadFile = new File(file.getFilePath(), file.getFileNm());
        if (!downloadFile.exists()) {
            throw new IOException("파일이 존재하지 않습니다: " + file.getFileOriginNm());
        }

        byte[] fileBytes = FileUtils.readFileToByteArray(downloadFile);

        response.setContentType("application/octet-stream");
        response.setContentLength(fileBytes.length);
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + URLEncoder.encode(file.getFileOriginNm(), "UTF-8") + "\"");
        response.setHeader("Content-Transfer-Encoding", "binary");

        response.getOutputStream().write(fileBytes);
        response.getOutputStream().flush();
        response.getOutputStream().close();

        log.info("파일 다운로드: {}", file.getFileOriginNm());
    }

    /**
     * 여러 파일 ZIP으로 다운로드
     */
    @Transactional(readOnly = true)
    public void downloadFilesAsZip(List<String> fileUids, HttpServletResponse response) throws IOException {
        List<AttachedFile> files = fileRepository.findByFileUidIn(fileUids);

        if (files.isEmpty()) {
            throw new IOException("다운로드할 파일이 없습니다.");
        }

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + URLEncoder.encode("files.zip", "UTF-8") + "\"");
        response.setHeader("Content-Transfer-Encoding", "binary");

        try (OutputStream outputStream = response.getOutputStream();
             ZipOutputStream zos = new ZipOutputStream(outputStream)) {

            for (AttachedFile fileInfo : files) {
                File file = new File(fileInfo.getFilePath(), fileInfo.getFileNm());
                if (file.exists() && file.isFile()) {
                    ZipEntry zipEntry = new ZipEntry(fileInfo.getFileOriginNm());
                    zos.putNextEntry(zipEntry);

                    byte[] fileBytes = FileUtils.readFileToByteArray(file);
                    zos.write(fileBytes);

                    zos.closeEntry();
                }
            }
            zos.finish();
        }

        log.info("ZIP 파일 다운로드 완료: {} 개 파일", files.size());
    }

    /**
     * 파일 삭제 (Soft Delete)
     */
    @Transactional
    public void deleteFile(String fileUid) {
        AttachedFile file = fileRepository.findByFileUidAndDelYn(fileUid, false)
                .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다: " + fileUid));

        file.softDelete();
        fileRepository.save(file);

        log.info("파일 삭제 (Soft Delete): {}", file.getFileOriginNm());
    }

    /**
     * 여러 파일 삭제
     */
    @Transactional
    public void deleteFiles(List<String> fileUids) {
        for (String fileUid : fileUids) {
            try {
                deleteFile(fileUid);
            } catch (Exception e) {
                log.error("파일 삭제 실패: {}", fileUid, e);
                throw new RuntimeException("파일 삭제 중 오류 발생: " + fileUid);
            }
        }
    }

    /**
     * 파일 물리적 삭제 (실제 파일 삭제)
     */
    @Transactional
    public void permanentDeleteFile(String fileUid) throws IOException {
        AttachedFile file = fileRepository.findById(fileUid)
                .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다: " + fileUid));

        // 물리적 파일 삭제
        File physicalFile = new File(file.getFilePath(), file.getFileNm());
        if (physicalFile.exists()) {
            if (!physicalFile.delete()) {
                throw new IOException("파일 삭제에 실패했습니다: " + file.getFileOriginNm());
            }
        }

        // DB 레코드 삭제
        fileRepository.delete(file);

        log.info("파일 영구 삭제: {}", file.getFileOriginNm());
    }

    /**
     * 에디터 이미지 업로드
     */
    @Transactional
    public AttachedFileDTO uploadEditorImage(MultipartFile file, String userId) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("업로드할 파일이 없습니다.");
        }

        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        // 이미지 파일만 허용
        if (!fileExtension.matches("\\.(jpg|jpeg|png|gif|svg)")) {
            throw new IOException("이미지 파일만 업로드 가능합니다.");
        }

        // 파일 저장 경로
        String basePath = editorimgPath;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String monthFolder = sdf.format(new Date());
        String filePath = basePath + "/" + monthFolder;

        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 파일 저장
        String fileUid = generateFileUid();
        String storedFileName = fileUid + fileExtension;
        File destFile = new File(filePath, storedFileName);
        file.transferTo(destFile);

        // DB에 저장하지 않고 DTO만 반환 (에디터 이미지는 별도 관리)
        return AttachedFileDTO.builder()
                .fileUid(fileUid)
                .fileOriginNm(originalFileName)
                .fileType(fileExtension)
                .filePath(filePath)
                .fileNm(storedFileName)
                .fileSize(file.getSize())
                .inputUserId(userId)
                .build();
    }
}