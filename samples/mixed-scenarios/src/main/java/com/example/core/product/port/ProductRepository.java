package com.example.core.product.port;

import com.example.core.product.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * ✅ GOOD: Port interface in core package
 * ✅ Passes: core_interfaces_should_be_implemented_in_adapters
 */
public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(String id);
    List<Product> findAll();
    void deleteById(String id);
}