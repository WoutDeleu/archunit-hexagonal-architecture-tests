package com.example.adapters.api.entity;

import com.example.core.book.model.Book;

import java.math.BigDecimal;

/**
 * Data Transfer Object for API responses - correctly placed in API entity package.
 * ✅ Passes: All adapter structure tests
 */
public class BookDto {
    private String id;
    private String title;
    private String author;
    private BigDecimal price;
    private String isbn;

    public BookDto() {}

    public BookDto(String id, String title, String author, BigDecimal price, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.isbn = isbn;
    }

    public static BookDto fromDomain(Book book) {
        return new BookDto(
                book.getId().toString(),
                book.getTitle(),
                book.getAuthor(),
                book.getPrice(),
                book.getIsbn()
        );
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
}