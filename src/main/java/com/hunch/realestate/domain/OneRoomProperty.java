package com.hunch.realestate.domain;

import com.hunch.realestate.common.enums.RoomType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// 원룸/투룸
@Getter
@Setter
public class OneRoomProperty extends Property {
    private Integer roomCount; // 방수
    private Integer bathCount; // 욕실수
    private RoomType roomType; // 방 타입(오픈형/분리형 등)
    private Boolean hasManagementFee; // 관리비포함여부
    private Long managementFee; // 관리비
    private List<String> options; // 옵션정보(가전/가구 등)
}
