package com.hunch.realestate.domain.dto;

import com.hunch.realestate.common.enums.PropertyType;
import com.hunch.realestate.common.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDTO {
    private String id;
    private PropertyType propertyType;
    private String propertyName;  // 매물명
    private String postalCode;  // 우편번호
    private String roadAddress;  // 도로명 주소
    private String jibunAddress;  // 지번 주소
    private String address;
    private String detailAddress;
    private Double latitude;
    private Double longitude;
    private TransactionType transactionType;
    private Long price;  // 매매가
    private Long deposit;  // 보증금 (전세/월세)
    private Long monthlyRent;  // 월세
    private Long maintenanceFee;  // 관리비
    private Double areaPyeong;  // 평수
    private Integer floor;  // 층수
    private String direction;  // 방향
    private String heatingType;  // 난방 종류
    private String moveInType;  // 입주 타입
    private LocalDate moveInDate;  // 입주 가능일
    private String bathroom;  // 화장실
    private String buildingUsage;  // 건축물 용도

    // 동적 필드 (매물 타입별)
    private String entranceStructure;  // 현관구조
    private String parkingAvailable;  // 주차가능여부
    private String isDuplex;  // 복층여부
    private String elevator;  // 엘리베이터
    private String businessRestriction;  // 업종제한

    private String directions;  // 오시는 길
    private String photoNote;  // 사진 메모
    private String managerContact;  // 담당자 연락처
    private String privateNotes;  // 관리자 메모
    private List<String> images = new ArrayList<>();
    private String description;
    private String status;  // 매물 상태
    private Boolean isVisible;  // 노출 여부
    private Boolean featured;  // 추천 매물
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // View에서 사용할 추가 필드들
    private Map<String, Object> additionalFields;  // 매물 타입별 추가 필드 (동적 필드)

    // 거래 유형에 따른 가격 정보 포맷팅
    public String getFormattedPrice() {
        if (transactionType == null) return "";

        switch (transactionType) {
            case SALE:
                return price != null ? price + "만원" : "";
            case JEONSE:
                return deposit != null ? deposit + "만원" : "";
            case MONTHLY:
                return String.format("%d/%d만원",
                        deposit != null ? deposit : 0,
                        monthlyRent != null ? monthlyRent : 0);
            default:
                return "";
        }
    }

    // 주소 포맷팅 (상세주소가 있는 경우 함께 표시)
    public String getFullAddress() {
        if (detailAddress != null && !detailAddress.trim().isEmpty()) {
            return String.format("%s %s", address, detailAddress);
        }
        return address;
    }

    // 대표 이미지 URL 반환
    public String getMainImageUrl() {
        if (images != null && !images.isEmpty()) {
            return images.get(0);
        }
        return "/images/no-image.jpg";  // 기본 이미지
    }

    // 추가 필드 설정
    public void setAdditionalField(String key, Object value) {
        if (this.additionalFields == null) {
            this.additionalFields = new HashMap<>();
        }
        this.additionalFields.put(key, value);
    }

    // 추가 필드 조회
    public Object getAdditionalField(String key) {
        if (this.additionalFields == null) {
            return null;
        }
        return this.additionalFields.get(key);
    }

    // 유효성 검사
    public Map<String, String> validate() {
        Map<String, String> errors = new HashMap<>();

        if (propertyType == null) {
            errors.put("propertyType", "매물 유형을 선택해주세요");
        }

        if (address == null || address.trim().isEmpty()) {
            errors.put("address", "주소를 입력해주세요");
        }

        if (latitude == null) {
            errors.put("latitude", "위도를 입력해주세요");
        }

        if (longitude == null) {
            errors.put("longitude", "경도를 입력해주세요");
        }

        if (transactionType == null) {
            errors.put("transactionType", "거래 유형을 선택해주세요");
        }

        if (transactionType != null) {
            switch (transactionType) {
                case SALE:
                    if (price == null || price <= 0) {
                        errors.put("price", "매매가를 입력해주세요");
                    }
                    break;
                case JEONSE:
                    if (deposit == null || deposit <= 0) {
                        errors.put("deposit", "전세금을 입력해주세요");
                    }
                    break;
                case MONTHLY:
                    if (deposit == null || deposit < 0) {
                        errors.put("deposit", "보증금을 입력해주세요");
                    }
                    if (monthlyRent == null || monthlyRent <= 0) {
                        errors.put("monthlyRent", "월세를 입력해주세요");
                    }
                    break;
            }
        }

        return errors;
    }
}