package com.hunch.realestate.common.enums;

public enum PropertyType {
    APARTMENT("아파트"),
    VILLA("빌라"),
    ONE_ROOM("원룸"),
    TWOTHREE_ROOM("투룸/쓰리룸"),
    COMMERCIAL("상가/사무실"),
    STORE("점포");

    public final String description;

    PropertyType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}