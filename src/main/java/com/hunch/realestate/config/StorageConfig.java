package com.hunch.realestate.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hunch.realestate.config.StorageProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import lombok.RequiredArgsConstructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
@RequiredArgsConstructor
public class StorageConfig {

    private final StorageProperties storageProperties;  // 필드 주입으로 변경

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeStorage(ApplicationReadyEvent event) throws IOException {  // 매개변수 타입 변경
        // 기본 디렉토리 생성
        Files.createDirectories(Path.of(storageProperties.getBasePath()));
        Files.createDirectories(Path.of(storageProperties.getFiles().getImages()));
        Files.createDirectories(Path.of(storageProperties.getFiles().getDocuments()));

        // JSON 파일이 없으면 빈 파일 생성
        createIfNotExists(storageProperties.getJson().getUsers());
        createIfNotExists(storageProperties.getJson().getProperties());
        createIfNotExists(storageProperties.getJson().getTransactions());
    }

    private void createIfNotExists(String filePath) throws IOException {
        Path path = Path.of(filePath);
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            Files.write(path, "[]".getBytes());
        }
    }
}