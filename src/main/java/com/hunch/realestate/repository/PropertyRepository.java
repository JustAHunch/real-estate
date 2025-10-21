package com.hunch.realestate.repository;

import com.hunch.realestate.common.enums.PropertyType;
import com.hunch.realestate.domain.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     * 상태별 조회
     */
    List<Property> findByStatus(String status);

    /**
     * 노출 여부별 조회
     */
    Page<Property> findByIsVisible(Boolean isVisible, Pageable pageable);

    /**
     * 매물 타입 및 상태별 조회
     */
    Page<Property> findByPropertyTypeAndStatus(PropertyType propertyType, String status, Pageable pageable);

    /**
     * 주소로 검색
     */
    @Query("SELECT p FROM Property p WHERE p.address LIKE %:keyword% OR p.detailAddress LIKE %:keyword%")
    Page<Property> searchByAddress(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 가격 범위로 검색
     */
    @Query("SELECT p FROM Property p WHERE " +
            "(p.price BETWEEN :minPrice AND :maxPrice) OR " +
            "(p.deposit BETWEEN :minPrice AND :maxPrice) OR " +
            "(p.monthlyRent BETWEEN :minPrice AND :maxPrice)")
    Page<Property> findByPriceRange(@Param("minPrice") Long minPrice, 
                                     @Param("maxPrice") Long maxPrice, 
                                     Pageable pageable);
}
