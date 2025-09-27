package com.example.adapters.database.entity;

import com.example.core.book.model.Book;
import com.example.core.book.model.BookId;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * JPA entity - correctly placed in database entity package.
 * ✅ Passes: jpa_entities_should_be_in_database_adapters
 * ✅ Passes: entities_should_not_use_spring_annotations (only JPA annotations)
 */
@Entity
@Table(name = "books")
public class BookEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, unique = true)
    private String isbn;

    public BookEntity() {}

    public BookEntity(UUID id, String title, String author, BigDecimal price, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.isbn = isbn;
    }

    public static BookEntity fromDomain(Book book) {
        return new BookEntity(
                book.getId().getValue(),
                book.getTitle(),
                book.getAuthor(),
                book.getPrice(),
                book.getIsbn()
        );
    }

    public Book toDomain() {
        return new Book(
                new BookId(this.id),
                this.title,
                this.author,
                this.price,
                this.isbn
        );
    }

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
}