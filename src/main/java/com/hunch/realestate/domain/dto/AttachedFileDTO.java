package com.hunch.realestate.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hunch.realestate.domain.entity.AttachedFile;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 첨부파일 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachedFileDTO {
    private String id;
    private String fileGrpId;
    private String fileUid;
    private String fileOriginNm;
    private String fileType;
    private String filePath;
    private String fileNm;
    private Long fileSize;
    private Double fileSizeInMB;
    private Boolean delYn;
    private String inputUserId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    /**
     * Entity를 DTO로 변환
     */
    public static AttachedFileDTO fromEntity(AttachedFile entity) {
        if (entity == null) return null;

        return AttachedFileDTO.builder()
                .id(entity.getId())
                .fileGrpId(entity.getFileGrpId())
                .fileUid(entity.getFileUid())
                .fileOriginNm(entity.getFileOriginNm())
                .fileType(entity.getFileType())
                .filePath(entity.getFilePath())
                .fileNm(entity.getFileNm())
                .fileSize(entity.getFileSize())
                .fileSizeInMB(entity.getFileSizeInMB())
                .delYn(entity.getDelYn())
                .inputUserId(entity.getInputUserId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * DTO를 Entity로 변환
     */
    public AttachedFile toEntity() {
        return AttachedFile.builder()
                .fileGrpId(this.fileGrpId)
                .fileUid(this.fileUid)
                .fileOriginNm(this.fileOriginNm)
                .fileType(this.fileType)
                .filePath(this.filePath)
                .fileNm(this.fileNm)
                .fileSize(this.fileSize)
                .delYn(this.delYn != null ? this.delYn : false)
                .inputUserId(this.inputUserId)
                .build();
    }
}