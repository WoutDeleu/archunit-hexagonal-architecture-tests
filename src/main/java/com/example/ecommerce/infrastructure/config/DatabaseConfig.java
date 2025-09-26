package com.example.ecommerce.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.ecommerce.adapters.database")
@EnableTransactionManagement
public class DatabaseConfig {
    // Database configuration
}