package com.hunch.realestate.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.hunch.realestate.common.enums.PropertyType;
import com.hunch.realestate.common.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

// 기본 매물 모델
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "propertyType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ApartmentProperty.class, name = "APARTMENT"),
    @JsonSubTypes.Type(value = VillaProperty.class, name = "VILLA"),
    @JsonSubTypes.Type(value = OneRoomProperty.class, name = "ONE_ROOM"),
    @JsonSubTypes.Type(value = CommercialProperty.class, name = "COMMERCIAL"),
    @JsonSubTypes.Type(value = StoreProperty.class, name = "STORE")
})
public abstract class Property {
    private String id;
    private PropertyType propertyType;
    private String address;
    private String detailAddress;
    private Double latitude;
    private Double longitude;
    private TransactionType transactionType;
    private Long price;
    private Long deposit;
    private Long monthlyRent;
    private List<String> images;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}