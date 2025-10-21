package com.hunch.realestate.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 매물 필드 enum
 * 매물 타입별 동적 필드를 정의
 */
@Getter
@RequiredArgsConstructor
public enum PropertyField {
    // 기본 필드
    AREA("평수", "areaPyeong", "number", "평"),
    FLOOR("층수", "floor", "number", "층"),
    DIRECTION("방향", "direction", "select", "동향,서향,남향,북향,북동향,남동향,남서향,북서향"),

    // 아파트/빌라/원룸 공통
    ENTRANCE_STRUCTURE("현관구조", "entranceStructure", "select", "계단식,복도식,복합식"),

    // 주차 관련
    PARKING_AVAILABLE("주차가능여부", "parkingAvailable", "select", "가능,불가능,유료"),

    // 원룸/투쓰리룸 특화
    IS_DUPLEX("복층여부", "isDuplex", "select", "복층,단층"),

    // 상업시설 특화
    ELEVATOR("엘리베이터", "elevator", "select", "있음,없음"),

    // 점포 특화
    BUSINESS_RESTRICTION("업종제한", "businessRestriction", "text", "");

    private final String displayName;    // 화면 표시명
    private final String fieldName;      // 실제 필드명 (camelCase)
    private final String fieldType;      // input 타입 (text, number, select)
    private final String options;        // select 옵션 (콤마로 구분)

    /**
     * 옵션을 배열로 반환
     */
    public String[] getOptionArray() {
        if (options == null || options.isEmpty()) {
            return new String[0];
        }
        return options.split(",");
    }

    /**
     * 필드명으로 PropertyField 찾기
     */
    public static PropertyField getByFieldName(String fieldName) {
        for (PropertyField field : values()) {
            if (field.getFieldName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }

    /**
     * 표시명으로 PropertyField 찾기
     */
    public static PropertyField getByDisplayName(String displayName) {
        for (PropertyField field : values()) {
            if (field.getDisplayName().equals(displayName)) {
                return field;
            }
        }
        return null;
    }
}