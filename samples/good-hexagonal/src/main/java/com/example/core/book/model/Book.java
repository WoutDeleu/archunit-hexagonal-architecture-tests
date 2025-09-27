package com.example.core.book.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Pure domain entity - no framework dependencies.
 * ✅ Passes: All core domain purity tests
 */
public class Book {
    private final BookId id;
    private final String title;
    private final String author;
    private final BigDecimal price;
    private final String isbn;

    public Book(BookId id, String title, String author, BigDecimal price, String isbn) {
        this.id = Objects.requireNonNull(id, "Book ID cannot be null");
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.author = Objects.requireNonNull(author, "Author cannot be null");
        this.price = Objects.requireNonNull(price, "Price cannot be null");
        this.isbn = Objects.requireNonNull(isbn, "ISBN cannot be null");
    }

    public BookId getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public BigDecimal getPrice() { return price; }
    public String getIsbn() { return isbn; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}