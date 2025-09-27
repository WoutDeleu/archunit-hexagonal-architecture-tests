package com.example.core.product.usecase;

import com.example.core.product.model.Product;
import com.example.core.product.port.ProductRepository;

import java.util.List;

/**
 * ✅ GOOD: Pure business logic with no framework dependencies
 * ✅ Passes: All core domain purity tests
 */
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product createProduct(String name, String description) {
        String id = java.util.UUID.randomUUID().toString();
        Product product = new Product(id, name, description);
        return repository.save(product);
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Product getProduct(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }
}