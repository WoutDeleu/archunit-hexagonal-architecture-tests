package com.example.core.book.exceptions;

/**
 * Domain-specific exception - no framework dependencies.
 * ✅ Passes: All core domain purity tests
 */
public class DuplicateIsbnException extends RuntimeException {
    public DuplicateIsbnException(String message) {
        super(message);
    }

    public DuplicateIsbnException(String message, Throwable cause) {
        super(message, cause);
    }
}