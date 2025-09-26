package com.example.ecommerce.core.product.port;

import com.example.ecommerce.core.product.model.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Optional<Product> findById(UUID id);
    List<Product> findAll();
    Product save(Product product);
    void deleteById(UUID id);
    List<Product> findByNameContaining(String name);
}