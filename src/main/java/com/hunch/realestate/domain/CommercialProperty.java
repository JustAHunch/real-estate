package com.hunch.realestate.domain;

import lombok.Getter;
import lombok.Setter;

// 상가/점포
@Getter
@Setter
public class CommercialProperty extends Property {
    private Long premium; // 권리금
    private String businessType; // 업종
    private String currentBusiness; // 현재업종
    private Double floorArea; // 바닥면적
    private String roadAccess; // 도로접근성
    private ProfitInfo profitInfo; // 수익정보
}