package com.example.ecommerce.violations;

import com.example.ecommerce.adapters.api.entity.ProductDto;
import com.example.ecommerce.adapters.database.entity.ProductEntity;
import org.springframework.stereotype.Service;

// VIOLATION: Cross-adapter dependency (api adapter depending on database adapter)
@Service
public class BadCrossDependency {

    public ProductDto convertEntity(ProductEntity entity) {
        // This is a violation - adapters should not depend on each other
        return new ProductDto(entity.getId(), entity.getName(),
                            entity.getDescription(), entity.getPrice(),
                            entity.getStockQuantity());
    }
}