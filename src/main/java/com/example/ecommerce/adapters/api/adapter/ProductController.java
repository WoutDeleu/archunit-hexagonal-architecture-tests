package com.example.ecommerce.adapters.api.adapter;

import com.example.ecommerce.adapters.api.entity.ProductDto;
import com.example.ecommerce.core.product.model.Product;
import com.example.ecommerce.core.product.usecase.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProductDto getProduct(@PathVariable UUID id) {
        Product product = productService.getProduct(id);
        return toDto(product);
    }

    @PostMapping
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        Product product = productService.createProduct(
                productDto.getName(),
                productDto.getDescription(),
                productDto.getPrice(),
                productDto.getStockQuantity()
        );
        return toDto(product);
    }

    @PutMapping("/{id}/stock")
    public ProductDto updateStock(@PathVariable UUID id, @RequestParam int quantity) {
        Product product = productService.updateStock(id, quantity);
        return toDto(product);
    }

    @GetMapping("/search")
    public List<ProductDto> searchProducts(@RequestParam String name) {
        return productService.searchProducts(name).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ProductDto toDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity()
        );
    }
}