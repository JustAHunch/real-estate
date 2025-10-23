package com.hunch.realestate.domain.entity;

import com.hunch.realestate.common.enums.PropertyType;
import com.hunch.realestate.common.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 매물 엔티티
 * 화면 필드: register.html, list.html 참조
 */
@Entity
@Table(name = "properties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property extends BaseEntity {

    /**
     * 매물 종류
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "property_type", nullable = false, length = 50)
    private PropertyType propertyType;

    /**
     * 거래 유형 (매매/전세/월세)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 50)
    private TransactionType transactionType;

    // ========== 기본 정보 ==========

    /**
     * 우편번호
     */
    @Column(name = "postal_code", length = 10)
    private String postalCode;

    /**
     * 도로명 주소
     */
    @Column(name = "road_address", length = 500)
    private String roadAddress;

    /**
     * 지번 주소
     */
    @Column(name = "jibun_address", length = 500)
    private String jibunAddress;

    /**
     * 주소 (기존 필드, 호환성 유지)
     */
    @Column(name = "address", nullable = false, length = 500)
    private String address;

    /**
     * 상세 주소
     */
    @Column(name = "detail_address", length = 500)
    private String detailAddress;

    /**
     * 위도
     */
    @Column(name = "latitude")
    private Double latitude;

    /**
     * 경도
     */
    @Column(name = "longitude")
    private Double longitude;

    // ========== 가격 정보 ==========

    /**
     * 매매가 (만원)
     */
    @Column(name = "price")
    private Long price;

    /**
     * 보증금 (만원)
     */
    @Column(name = "deposit")
    private Long deposit;

    /**
     * 월세 (만원)
     */
    @Column(name = "monthly_rent")
    private Long monthlyRent;

    /**
     * 관리비 (만원)
     */
    @Column(name = "maintenance_fee")
    private Long maintenanceFee;

    // ========== 매물 상세 정보 ==========

    /**
     * 난방 종류
     */
    @Column(name = "heating_type", length = 100)
    private String heatingType;

    /**
     * 방향 (동향, 서향, 남향 등)
     */
    @Column(name = "direction", length = 50)
    private String direction;

    /**
     * 화장실 (외부/내부, 남여구분/여호용)
     */
    @Column(name = "bathroom", length = 100)
    private String bathroom;

    /**
     * 건축물 용도
     */
    @Column(name = "building_usage", length = 200)
    private String buildingUsage;

    /**
     * 평수
     */
    @Column(name = "area_pyeong")
    private Double areaPyeong;

    /**
     * 층수
     */
    @Column(name = "floor")
    private Integer floor;

    // ========== 입주 정보 ==========

    /**
     * 입주 타입 (즉시/협의/날짜지정)
     */
    @Column(name = "move_in_type", length = 50)
    private String moveInType;

    /**
     * 입주 날짜
     */
    @Column(name = "move_in_date")
    private LocalDate moveInDate;

    // ========== 남세 정보 (공개 시간) ==========

    /**
     * 오전 시작 시간
     */
    @Column(name = "morning_start_time")
    private LocalTime morningStartTime;

    /**
     * 오전 종료 시간
     */
    @Column(name = "morning_end_time")
    private LocalTime morningEndTime;

    /**
     * 오후 시작 시간
     */
    @Column(name = "afternoon_start_time")
    private LocalTime afternoonStartTime;

    /**
     * 오후 종료 시간
     */
    @Column(name = "afternoon_end_time")
    private LocalTime afternoonEndTime;

    // ========== 설명 정보 ==========

    /**
     * 오시는 길
     */
    @Column(name = "directions", length = 1000)
    private String directions;

    /**
     * 상세 설명
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 사진 메모 (등사사진 내용)
     */
    @Column(name = "photo_note", length = 500)
    private String photoNote;

    /**
     * 담당자 이름/연락처
     */
    @Column(name = "manager_contact", length = 200)
    private String managerContact;

    // ========== 비공개 정보 ==========

    /**
     * 관리자 메모
     */
    @Column(name = "private_notes", columnDefinition = "TEXT")
    private String privateNotes;

    // ========== 이미지 ==========

    /**
     * 대표 이미지 URL
     */
    @Column(name = "main_image_url", length = 500)
    private String mainImageUrl;

    /**
     * 추가 사진 URL (콤마로 구분)
     */
    @Column(name = "photo_urls", columnDefinition = "TEXT")
    private String photoUrls;

    // ========== 상태 정보 ==========

    /**
     * 매물 상태 (판매중, 예약중, 거래완료)
     */
    @Column(name = "status", length = 50)
    @Builder.Default
    private String status = "판매중";

    /**
     * 노출 여부
     */
    @Column(name = "is_visible")
    @Builder.Default
    private Boolean isVisible = true;
}
