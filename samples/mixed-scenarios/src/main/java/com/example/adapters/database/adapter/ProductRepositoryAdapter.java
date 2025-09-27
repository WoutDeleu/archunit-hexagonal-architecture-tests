package com.example.adapters.database.adapter;

import com.example.adapters.database.entity.ProductEntity;
import com.example.core.product.model.Product;
import com.example.core.product.port.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ✅ GOOD: Repository adapter correctly implementing core port
 * ✅ Passes: repositories_should_be_in_database_adapters
 * ✅ Passes: database_adapters_should_implement_core_interfaces
 */
@Service
public class ProductRepositoryAdapter implements ProductRepository {
    // Simple in-memory storage for demo
    private final java.util.Map<String, ProductEntity> storage = new java.util.HashMap<>();

    @Override
    public Product save(Product product) {
        ProductEntity entity = ProductEntity.fromDomain(product);
        storage.put(entity.getId(), entity);
        return entity.toDomain();
    }

    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(storage.get(id))
                .map(ProductEntity::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return storage.values().stream()
                .map(ProductEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        storage.remove(id);
    }
}