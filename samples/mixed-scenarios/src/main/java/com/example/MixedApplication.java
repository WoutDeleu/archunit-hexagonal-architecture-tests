package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ✅ GOOD: Spring Boot application correctly placed in root package
 * ✅ Passes: spring_boot_application_should_be_in_root
 */
@SpringBootApplication
public class MixedApplication {
    public static void main(String[] args) {
        SpringApplication.run(MixedApplication.class, args);
    }
}