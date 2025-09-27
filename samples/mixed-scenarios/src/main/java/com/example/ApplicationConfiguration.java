package com.example;

import com.example.core.product.port.ProductRepository;
import com.example.core.product.usecase.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ✅ GOOD: Configuration class correctly placed at application root level
 * ✅ Demonstrates: Proper placement for configuration that creates core beans
 * ✅ Follows hexagonal architecture: Configuration at application boundary, not in infrastructure
 */
@Configuration
public class ApplicationConfiguration {

    @Bean
    public ProductService productService(ProductRepository productRepository) {
        return new ProductService(productRepository);
    }
}