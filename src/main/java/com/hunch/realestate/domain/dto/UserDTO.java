package com.hunch.realestate.domain.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor                // 추가: Jackson이 역직렬화할 때 필요
@AllArgsConstructor               // 추가: @Builder와 함께 사용
public class UserDTO {
    private String username;
    private String password;
    private List<String> roles;
}