
package com.hunch.realestate.domain;

import lombok.Getter;
import lombok.Setter;

// 아파트/빌라 매물
@Getter
@Setter
public class ApartmentProperty extends Property {
    private Double supplyArea; // 공급면적
    private Double exclusiveArea; // 전용면적
    private Integer floor; // 해당층
    private Integer totalFloors; // 총층수
    private String heatingType; // 난방방식
    private Boolean hasParking; // 주차가능
    private Integer parkingCount; // 주차대수
    private Boolean hasElevator; // 엘리베이터
}