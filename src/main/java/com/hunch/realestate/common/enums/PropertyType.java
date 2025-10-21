package com.hunch.realestate.common.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * 매물 타입 enum
 * 각 타입별 동적 필드 매핑 포함
 */
@Getter
public enum PropertyType {
    APARTMENT("아파트",
        PropertyField.AREA, PropertyField.FLOOR, PropertyField.DIRECTION, PropertyField.ENTRANCE_STRUCTURE),

    VILLA("빌라",
        PropertyField.AREA, PropertyField.FLOOR, PropertyField.DIRECTION, PropertyField.PARKING_AVAILABLE),

    ONE_ROOM("원룸",
        PropertyField.AREA, PropertyField.FLOOR, PropertyField.DIRECTION, PropertyField.IS_DUPLEX, PropertyField.PARKING_AVAILABLE),

    TWOTHREE_ROOM("투룸/쓰리룸",
        PropertyField.AREA, PropertyField.FLOOR, PropertyField.DIRECTION, PropertyField.IS_DUPLEX, PropertyField.PARKING_AVAILABLE),

    COMMERCIAL("상가/사무실",
        PropertyField.AREA, PropertyField.FLOOR, PropertyField.ELEVATOR, PropertyField.PARKING_AVAILABLE),

    STORE("점포",
        PropertyField.AREA, PropertyField.FLOOR, PropertyField.BUSINESS_RESTRICTION);

    private final String description;
    private final List<PropertyField> fields;

    PropertyType(String description, PropertyField... fields) {
        this.description = description;
        this.fields = Arrays.asList(fields);
    }

    /**
     * 필드 표시명 리스트 반환
     */
    public List<String> getFieldDisplayNames() {
        return fields.stream()
                .map(PropertyField::getDisplayName)
                .toList();
    }

    /**
     * 필드명 리스트 반환 (camelCase)
     */
    public List<String> getFieldNames() {
        return fields.stream()
                .map(PropertyField::getFieldName)
                .toList();
    }
}