package com.example.ecommerce.violations;

import org.springframework.stereotype.Repository;
import com.example.ecommerce.core.product.model.Product;
import java.util.UUID;

// VIOLATION: Repository implementation in wrong package (should be in adapters.database)
@Repository
public class BadRepositoryInCore {

    public Product findById(UUID id) {
        return null; // This should not be in core!
    }
}