package com.example.ecommerce.violations;

import org.springframework.stereotype.Service;
import com.example.ecommerce.core.product.model.Product;
import java.util.UUID;

// VIOLATION: Core domain class using Spring annotations
@Service
public class BadCoreWithSpringAnnotations {

    public Product createProduct(String name) {
        return new Product(UUID.randomUUID(), name, "Description", null, 0);
    }
}