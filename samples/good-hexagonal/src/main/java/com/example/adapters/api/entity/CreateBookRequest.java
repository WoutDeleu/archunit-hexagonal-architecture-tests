package com.example.adapters.api.entity;

import java.math.BigDecimal;

/**
 * Request DTO for creating books - correctly placed in API entity package.
 * ✅ Passes: All adapter structure tests
 */
public class CreateBookRequest {
    private String title;
    private String author;
    private BigDecimal price;
    private String isbn;

    public CreateBookRequest() {}

    public CreateBookRequest(String title, String author, BigDecimal price, String isbn) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.isbn = isbn;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
}