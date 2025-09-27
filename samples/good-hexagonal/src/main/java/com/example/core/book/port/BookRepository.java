package com.example.core.book.port;

import com.example.core.book.model.Book;
import com.example.core.book.model.BookId;

import java.util.List;
import java.util.Optional;

/**
 * Port interface for book persistence - pure interface in core.
 * ✅ Passes: core_interfaces_should_be_implemented_in_adapters
 */
public interface BookRepository {

    Book save(Book book);

    Optional<Book> findById(BookId id);

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByAuthor(String author);

    List<Book> findAll();

    void deleteById(BookId id);

    boolean existsById(BookId id);
}