package com.hunch.realestate.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

/**
 * 부동산 회사 정보 엔티티
 * 화면 필드: company-info.html 참조
 */
@Entity
@Table(name = "company_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Comment("부동산 회사 정보")
public class CompanyInfo extends BaseEntity {

    /**
     * 상호명
     */
    @Column(name = "business_name", nullable = false, length = 200)
    @Comment("상호명")
    private String businessName;

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

    /**
     * 찾아오는 길
     */
    @Column(name = "directions", length = 1000)
    @Comment("찾아오는 길")
    private String directions;

    /**
     * 전화번호
     */
    @Column(name = "phone_number", length = 50)
    @Comment("대표 전화번호")
    private String phoneNumber;

    /**
     * 팩스 번호
     */
    @Column(name = "fax_number", length = 50)
    @Comment("팩스 번호")
    private String faxNumber;

    /**
     * 담당자 직급
     */
    @Column(name = "manager_position", length = 100)
    @Comment("담당자 직급")
    private String managerPosition;

    /**
     * 담당자 이름
     */
    @Column(name = "manager_name", length = 100)
    @Comment("담당자 이름")
    private String managerName;

    /**
     * 담당자 연락처
     */
    @Column(name = "manager_phone", length = 50)
    @Comment("담당자 연락처")
    private String managerPhone;

    /**
     * 담당자 이메일
     */
    @Column(name = "manager_email", length = 200)
    @Comment("담당자 이메일")
    private String managerEmail;

    /**
     * 담당자 사진 URL
     */
    @Column(name = "manager_photo", length = 500)
    @Comment("담당자 사진 URL")
    private String managerPhoto;
}
