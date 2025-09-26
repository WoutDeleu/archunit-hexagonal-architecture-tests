package com.example.ecommerce.core.product.usecase;

import com.example.ecommerce.core.product.model.Product;
import com.example.ecommerce.core.product.port.ProductRepository;
import com.example.ecommerce.core.product.exceptions.ProductNotFoundException;
import com.example.ecommerce.core.product.exceptions.InsufficientStockException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(String name, String description, BigDecimal price, int stockQuantity) {
        Product product = new Product(UUID.randomUUID(), name, description, price, stockQuantity);
        return productRepository.save(product);
    }

    public Product getProduct(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateStock(UUID productId, int newQuantity) {
        Product product = getProduct(productId);
        if (newQuantity < 0) {
            throw new InsufficientStockException("Stock quantity cannot be negative");
        }
        Product updatedProduct = product.updateStock(newQuantity);
        return productRepository.save(updatedProduct);
    }

    public List<Product> searchProducts(String name) {
        return productRepository.findByNameContaining(name);
    }
}