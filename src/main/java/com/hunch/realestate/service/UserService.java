package com.hunch.realestate.service;

import com.hunch.realestate.config.StorageProperties;
import com.hunch.realestate.domain.dto.UserDTO;
import org.springframework.stereotype.Service;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final JsonStorageService jsonStorageService;
    private final StorageProperties storageProperties;
    
    @EventListener(ApplicationReadyEvent.class)
    public void initializeDefaultUser() {
        String usersPath = storageProperties.getJson().getUsers();
        List<UserDTO> users = jsonStorageService.readList(usersPath, new TypeReference<List<UserDTO>>() {});
        
        // 기본 관리자가 없으면 생성
        if (users.stream().noneMatch(u -> u.getUsername().equals("admin"))) {
            UserDTO admin = UserDTO.builder()
                .username("admin")
                .password("admin123")
                .roles(List.of("ADMIN", "USER"))
                .build();
            
            users = new ArrayList<>(users);  // 빈 리스트인 경우를 위한 초기화
            users.add(admin);
            jsonStorageService.writeList(usersPath, users);
        }
    }
}
