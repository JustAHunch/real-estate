package com.hunch.realestate.domain;

import lombok.Getter;
import lombok.Setter;

// 점포 매물 - 상가와 구분되는 특성 반영
@Getter
@Setter
public class StoreProperty extends Property {
    private Long premium; // 권리금
    private String businessType; // 업종
    private String currentBusiness; // 현재업종
    private Double floorArea; // 바닥면적
    private String roadAccess; // 도로접근성
    private String storeFront; // 전면 길이
    private Boolean hasShutter; // 셔터 유무
    private String previousBusiness; // 전업종
    private String signboardCondition; // 간판 상태
    private Boolean hasBackEntrance; // 후문 유무
    private ProfitInfo profitInfo; // 수익정보
    private Double ceilingHeight; // 천장높이
    private String waterSupply; // 급수시설
}