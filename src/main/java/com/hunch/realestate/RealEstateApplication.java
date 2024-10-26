package com.hunch.realestate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RealEstateApplication {
    public static void main(String[] args) {
        SpringApplication.run(RealEstateApplication.class, args);
    }
}