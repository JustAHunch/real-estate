package com.hunch.realestate.domain.entity;

import com.hunch.realestate.common.enums.PropertyType;
import com.hunch.realestate.common.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

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
@Comment("매물 정보")
public class Property extends BaseEntity {

    /**
     * 매물 종류
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "property_type", nullable = false, length = 50)
    @Comment("매물 종류 (아파트/빌라/원룸/투룸/쓰리룸/상가/점포)")
    private PropertyType propertyType;

    /**
     * 거래 유형 (매매/전세/월세)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 50)
    @Comment("거래 유형 (매매/전세/월세)")
    private TransactionType transactionType;

    // ========== 기본 정보 ==========

    /**
     * 매물명
     */
    @Column(name = "property_name", length = 200)
    @Comment("매물명")
    private String propertyName;

    /**
     * 우편번호
     */
    @Column(name = "postal_code", length = 10)
    @Comment("우편번호")
    private String postalCode;

    /**
     * 도로명 주소
     */
    @Column(name = "road_address", length = 500)
    @Comment("도로명 주소")
    private String roadAddress;

    /**
     * 지번 주소
     */
    @Column(name = "jibun_address", length = 500)
    @Comment("지번 주소")
    private String jibunAddress;

    /**
     * 주소 (기존 필드, 호환성 유지)
     */
    @Column(name = "address", nullable = false, length = 500)
    @Comment("기본 주소")
    private String address;

    /**
     * 상세 주소
     */
    @Column(name = "detail_address", length = 500)
    @Comment("상세 주소")
    private String detailAddress;

    /**
     * 위도
     */
    @Column(name = "latitude")
    @Comment("위도")
    private Double latitude;

    /**
     * 경도
     */
    @Column(name = "longitude")
    @Comment("경도")
    private Double longitude;

    // ========== 가격 정보 ==========

    /**
     * 매매가 (만원)
     */
    @Column(name = "price")
    @Comment("매매가 (만원)")
    private Long price;

    /**
     * 보증금 (만원)
     */
    @Column(name = "deposit")
    @Comment("보증금 (만원)")
    private Long deposit;

    /**
     * 월세 (만원)
     */
    @Column(name = "monthly_rent")
    @Comment("월세 (만원)")
    private Long monthlyRent;

    /**
     * 관리비 (만원)
     */
    @Column(name = "maintenance_fee")
    @Comment("관리비 (만원)")
    private Long maintenanceFee;

    // ========== 매물 상세 정보 ==========

    /**
     * 난방 종류
     */
    @Column(name = "heating_type", length = 100)
    @Comment("난방 종류")
    private String heatingType;

    /**
     * 방향 (동향, 서향, 남향 등)
     */
    @Column(name = "direction", length = 50)
    @Comment("방향 (동향/서향/남향/북향)")
    private String direction;

    /**
     * 화장실 (외부/내부, 남여구분/여호용)
     */
    @Column(name = "bathroom", length = 100)
    @Comment("화장실 (외부/내부, 남여구분/여호용)")
    private String bathroom;

    /**
     * 건축물 용도
     */
    @Column(name = "building_usage", length = 200)
    @Comment("건축물 용도")
    private String buildingUsage;

    /**
     * 평수
     */
    @Column(name = "area_pyeong")
    @Comment("평수")
    private Double areaPyeong;

    /**
     * 층수
     */
    @Column(name = "floor")
    @Comment("층수")
    private Integer floor;

    /**
     * 현관구조 (아파트)
     */
    @Column(name = "entrance_structure", length = 50)
    @Comment("현관구조 (계단식/복도식/복합식)")
    private String entranceStructure;

    /**
     * 주차가능여부
     */
    @Column(name = "parking_available", length = 50)
    @Comment("주차가능여부 (가능/불가능/유료)")
    private String parkingAvailable;

    /**
     * 복층여부 (원룸/투룸)
     */
    @Column(name = "is_duplex", length = 50)
    @Comment("복층여부 (복층/단층)")
    private String isDuplex;

    /**
     * 엘리베이터 (상가)
     */
    @Column(name = "elevator", length = 50)
    @Comment("엘리베이터 (있음/없음)")
    private String elevator;

    /**
     * 업종제한 (점포)
     */
    @Column(name = "business_restriction", length = 200)
    @Comment("업종제한")
    private String businessRestriction;

    // ========== 입주 정보 ==========

    /**
     * 입주 타입 (즉시/협의/날짜지정)
     */
    @Column(name = "move_in_type", length = 50)
    @Comment("입주 타입 (즉시/협의/날짜지정)")
    private String moveInType;

    /**
     * 입주 날짜
     */
    @Column(name = "move_in_date")
    @Comment("입주 가능일")
    private LocalDate moveInDate;

    // ========== 설명 정보 ==========

    /**
     * 오시는 길
     */
    @Column(name = "directions", length = 1000)
    @Comment("오시는 길 안내")
    private String directions;

    /**
     * 상세 설명
     */
    @Column(name = "description", columnDefinition = "TEXT")
    @Comment("매물 상세 설명")
    private String description;

    /**
     * 사진 메모 (등사사진 내용)
     */
    @Column(name = "photo_note", length = 500)
    @Comment("사진 메모")
    private String photoNote;

    /**
     * 담당자 이름/연락처
     */
    @Column(name = "manager_contact", length = 200)
    @Comment("담당자 이름/연락처")
    private String managerContact;

    // ========== 비공개 정보 ==========

    /**
     * 관리자 메모
     */
    @Column(name = "private_notes", columnDefinition = "TEXT")
    @Comment("관리자 메모 (비공개)")
    private String privateNotes;

    // ========== 이미지 ==========

    /**
     * 대표 이미지 URL
     */
    @Column(name = "main_image_url", length = 500)
    @Comment("대표 이미지 URL")
    private String mainImageUrl;

    /**
     * 추가 사진 URL (콤마로 구분)
     */
    @Column(name = "photo_urls", columnDefinition = "TEXT")
    @Comment("추가 사진 URL (콤마 구분)")
    private String photoUrls;

    // ========== 상태 정보 ==========

    /**
     * 매물 상태 (판매중, 예약중, 거래완료)
     */
    @Column(name = "status", length = 50)
    @Comment("매물 상태 (판매중/예약중/거래완료)")
    @Builder.Default
    private String status = "판매중";

    /**
     * 노출 여부
     */
    @Column(name = "is_visible")
    @Comment("노출 여부")
    @Builder.Default
    private Boolean isVisible = true;

    /**
     * 추천 매물 여부
     */
    @Column(name = "featured")
    @Comment("추천 매물 여부 (메인 페이지 노출)")
    @Builder.Default
    private Boolean featured = false;
}
