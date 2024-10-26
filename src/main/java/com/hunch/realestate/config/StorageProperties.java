package com.hunch.realestate.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.storage")
public class StorageProperties {
    private String basePath;
    private JsonPaths json = new JsonPaths();
    private FilePaths files = new FilePaths();

    @Getter
    @Setter
    public static class JsonPaths {
        private String users;
        private String properties;
        private String transactions;
    }

    @Getter
    @Setter
    public static class FilePaths {
        private String images;
        private String documents;
    }
}