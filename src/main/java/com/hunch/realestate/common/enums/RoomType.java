package com.hunch.realestate.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

// 방 타입 Enum
public enum RoomType {
    OPEN("오픈형", "원룸"),
    SEMI_OPEN("세미오픈형", "방1 + 거실/주방"),
    SEPARATE("분리형", "방1 + 거실 + 주방"),
    DUPLEX("복층형", "복층구조"),
    TERRACE("테라스형", "테라스/베란다 포함"),
    LOFT("로프트형", "로프트형 구조"),
    TWO_ROOM("투룸", "방2"),
    THREE_ROOM("쓰리룸", "방3"),
    STUDIO("스튜디오형", "스튜디오 구조");

    private final String description;
    private final String details;

    RoomType(String description, String details) {
        this.description = description;
        this.details = details;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    public String getDetails() {
        return details;
    }

    @JsonCreator
    public static RoomType from(String value) {
        for (RoomType type : RoomType.values()) {
            if (type.description.equals(value) || type.name().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown RoomType: " + value);
    }
}