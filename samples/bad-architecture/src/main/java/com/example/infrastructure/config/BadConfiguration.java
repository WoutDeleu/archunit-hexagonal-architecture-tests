package com.example.infrastructure.config;

import com.example.core.user.UserService;
import com.example.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ❌ BAD: Configuration class that creates core beans placed in infrastructure/config
 * ❌ Violates: configuration_classes_that_create_core_beans_should_not_be_in_infrastructure_config
 * ❌ Problem: Configuration that wires core domain should be at application root or in core domain, not infrastructure
 */
@Configuration
public class BadConfiguration {

    @Bean
    public UserService userService() {
        return new UserService();
    }
}