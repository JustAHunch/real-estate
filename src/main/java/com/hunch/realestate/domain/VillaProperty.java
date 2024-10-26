package com.hunch.realestate.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// 빌라 매물
@Getter
@Setter
public class VillaProperty extends Property {
    private Double supplyArea; // 공급면적
    private Double exclusiveArea; // 전용면적
    private Integer floor; // 해당층
    private Integer totalFloors; // 총층수
    private String heatingType; // 난방방식
    private Boolean hasParking; // 주차가능
    private Boolean hasElevator; // 엘리베이터
    private String buildingStructure; // 건물구조(조적/철근콘크리트 등)
    private LocalDate approvalDate; // 사용승인일
    private Boolean hasBalcony; // 발코니 유무
    private Boolean isExtended; // 확장여부
    private String entranceType; // 현관구조(계단식/복도식)
}
