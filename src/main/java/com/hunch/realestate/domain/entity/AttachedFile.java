package com.hunch.realestate.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

/**
 * 첨부파일 엔티티
 */
@Entity
@Table(name = "attached_files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Comment("첨부파일")
public class AttachedFile extends BaseEntity {

    /**
     * 파일 그룹 ID
     */
    @Column(name = "file_grp_id", length = 50, nullable = false)
    @Comment("파일 그룹 ID")
    private String fileGrpId;

    /**
     * 원본 파일명
     */
    @Column(name = "file_origin_nm", length = 500, nullable = false)
    @Comment("원본 파일명")
    private String fileOriginNm;

    /**
     * 파일 타입 (확장자)
     */
    @Column(name = "file_type", length = 50)
    @Comment("파일 타입 (확장자)")
    private String fileType;

    /**
     * 파일 저장 경로
     */
    @Column(name = "file_path", length = 1000, nullable = false)
    @Comment("파일 저장 경로")
    private String filePath;

    /**
     * 저장된 파일명 (UUID + 확장자)
     */
    @Column(name = "file_nm", length = 500, nullable = false)
    @Comment("저장된 파일명 (UUID + 확장자)")
    private String fileNm;

    /**
     * 파일 크기 (bytes)
     */
    @Column(name = "file_size")
    @Comment("파일 크기 (bytes)")
    private Long fileSize;

    /**
     * 삭제 여부
     */
    @Column(name = "is_del")
    @Comment("삭제 여부 (true: 삭제됨, false: 사용중)")
    @Builder.Default
    private Boolean isDel = false;

    /**
     * 등록 사용자 ID
     */
    @Column(name = "input_user_id", length = 100)
    @Comment("등록 사용자 ID")
    private String inputUserId;

    /**
     * 소프트 삭제 (삭제 플래그 변경)
     */
    public void softDelete() {
        this.isDel = true;
    }

    /**
     * 파일 전체 경로 반환
     */
    public String getFullPath() {
        return filePath + "/" + fileNm;
    }

    /**
     * 파일 크기를 MB 단위로 반환
     */
    public double getFileSizeInMB() {
        if (fileSize == null) return 0;
        return Math.round(fileSize / (1024.0 * 1024.0) * 100.0) / 100.0;
    }
}