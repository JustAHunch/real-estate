package com.hunch.realestate.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hunch.realestate.config.StorageProperties;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
@Service
@RequiredArgsConstructor
public class JsonStorageService {
    private final ObjectMapper objectMapper;
    private final StorageProperties storageProperties;
    
    // 파일 액세스를 위한 락
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    
    // 제네릭 읽기 메서드
    public <T> List<T> readList(String filePath, TypeReference<List<T>> typeReference) {
        lock.readLock().lock();
        try {
            Path path = Path.of(filePath);
            if (!Files.exists(path)) {
                return List.of();
            }
            return objectMapper.readValue(path.toFile(), typeReference);
        } catch (Exception e) {
            log.error("Error reading from JSON file: {}", filePath, e);
            throw new RuntimeException("Failed to read data", e);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    // 제네릭 쓰기 메서드
    public <T> void writeList(String filePath, List<T> data) {
        lock.writeLock().lock();
        try {
            Path path = Path.of(filePath);
            objectMapper.writeValue(path.toFile(), data);
        } catch (Exception e) {
            log.error("Error writing to JSON file: {}", filePath, e);
            throw new RuntimeException("Failed to write data", e);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    // 파일 업로드 처리
    public String saveFile(byte[] content, String filename, String directory) {
        try {
            Path targetPath = Path.of(directory, filename);
            Files.write(targetPath, content);
            return targetPath.toString();
        } catch (Exception e) {
            log.error("Error saving file: {}", filename, e);
            throw new RuntimeException("Failed to save file", e);
        }
    }
}
