package com.example.core.book.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Value object for Book ID - pure domain object.
 * ✅ Passes: All core domain purity tests
 */
public class BookId {
    private final UUID value;

    public BookId(UUID value) {
        this.value = Objects.requireNonNull(value, "BookId value cannot be null");
    }

    public static BookId generate() {
        return new BookId(UUID.randomUUID());
    }

    public static BookId from(String value) {
        return new BookId(UUID.fromString(value));
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookId bookId = (BookId) o;
        return Objects.equals(value, bookId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}