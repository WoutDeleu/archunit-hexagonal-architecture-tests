package com.example.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ❌ VIOLATION: Spring Boot application NOT in root package
 * ❌ Fails: spring_boot_application_should_be_in_root
 */
@SpringBootApplication
public class BadApplication {
    public static void main(String[] args) {
        SpringApplication.run(BadApplication.class, args);
    }
}