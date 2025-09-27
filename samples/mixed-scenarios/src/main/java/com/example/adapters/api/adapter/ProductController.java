package com.example.adapters.api.adapter;

import com.example.core.product.model.Product;
import com.example.core.product.usecase.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ✅ GOOD: Controller correctly placed in adapters.api.adapter
 * ✅ Passes: controllers_should_be_in_api_adapters
 * ✅ Passes: controllers_should_use_core_port_interfaces
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product createProduct(@RequestBody CreateProductRequest request) {
        return productService.createProduct(request.getName(), request.getDescription());
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable String id) {
        return productService.getProduct(id);
    }

    public static class CreateProductRequest {
        private String name;
        private String description;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}