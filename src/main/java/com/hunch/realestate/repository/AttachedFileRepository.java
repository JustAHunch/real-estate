package com.hunch.realestate.repository;

import com.hunch.realestate.domain.entity.AttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 첨부파일 Repository
 */
@Repository
public interface AttachedFileRepository extends JpaRepository<AttachedFile, String> {

    /**
     * 파일 UID로 조회 (삭제되지 않은 파일만)
     */
    Optional<AttachedFile> findByFileUidAndDelYn(String fileUid, Boolean delYn);

    /**
     * 파일 그룹 ID로 조회 (삭제되지 않은 파일만)
     */
    @Query("SELECT f FROM AttachedFile f WHERE f.fileGrpId = :fileGrpId AND f.delYn = false ORDER BY f.createdAt")
    List<AttachedFile> findByFileGrpId(@Param("fileGrpId") String fileGrpId);

    /**
     * 파일 UID 목록으로 조회 (삭제되지 않은 파일만)
     */
    @Query("SELECT f FROM AttachedFile f WHERE f.fileUid IN :fileUids AND f.delYn = false")
    List<AttachedFile> findByFileUidIn(@Param("fileUids") List<String> fileUids);

    /**
     * 파일 UID 존재 여부 확인
     */
    boolean existsByFileUid(String fileUid);
}