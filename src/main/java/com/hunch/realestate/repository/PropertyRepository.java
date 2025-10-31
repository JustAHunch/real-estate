package com.hunch.realestate.repository;

import com.hunch.realestate.common.enums.PropertyType;
import com.hunch.realestate.domain.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 매물 Repository
 */
@Repository
public interface PropertyRepository extends JpaRepository<Property, String> {

    /**
     * 매물 타입별 조회 (페이징)
     */
    Page<Property> findByPropertyType(PropertyType propertyType, Pageable pageable);

    /**
     * 전체 조회 (페이징)
     */
    Page<Property> findAll(Pageable pageable);
}
