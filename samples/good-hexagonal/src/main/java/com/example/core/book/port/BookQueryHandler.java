package com.example.core.book.port;

import com.example.core.book.model.Book;

import java.util.List;

/**
 * Input port for book queries - defines contract for external queries.
 * ✅ Passes: core_interfaces_should_be_implemented_in_adapters
 * ✅ This will be implemented by the API adapter (BookController)
 */
public interface BookQueryHandler {

    List<Book> getAllBooks();

    List<Book> getBooksByAuthor(String author);
}