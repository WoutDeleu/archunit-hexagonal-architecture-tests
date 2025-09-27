package com.example.core.book.exceptions;

/**
 * Domain-specific exception - no framework dependencies.
 * ✅ Passes: All core domain purity tests
 */
public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String message) {
        super(message);
    }

    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}