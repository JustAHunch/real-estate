package com.hunch.realestate.common.enums;

public enum TransactionType {
    SALE("매매"),
    JEONSE("전세"),
    MONTHLY("월세");

    private final String description;
    
    TransactionType(String description) {
        this.description = description;
    }
}