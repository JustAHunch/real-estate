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
     * 파일 그룹 ID로 조회 (삭제되지 않은 파일만)
     */
    @Query("SELECT f FROM AttachedFile f WHERE f.fileGrpId = :fileGrpId AND f.isDel = false ORDER BY f.createdAt")
    List<AttachedFile> findByFileGrpId(@Param("fileGrpId") String fileGrpId);
}