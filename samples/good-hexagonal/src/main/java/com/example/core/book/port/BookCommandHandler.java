package com.example.core.book.port;

import com.example.core.book.model.Book;

/**
 * Input port for book commands - defines contract for external requests.
 * ✅ Passes: core_interfaces_should_be_implemented_in_adapters
 * ✅ This will be implemented by the API adapter (BookController)
 */
public interface BookCommandHandler {

    Book createBook(CreateBookCommand command);

    Book getBookById(String id);

    Book getBookByIsbn(String isbn);

    void deleteBook(String id);

    // Command objects for input validation and structure
    record CreateBookCommand(String title, String author, String price, String isbn) {}
}