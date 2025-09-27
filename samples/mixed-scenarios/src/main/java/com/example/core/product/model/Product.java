package com.example.core.product.model;

/**
 * ✅ GOOD: Pure domain entity with no framework dependencies
 * ✅ Passes: All core domain purity tests
 */
public class Product {
    private final String id;
    private final String name;
    private final String description;

    public Product(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
}