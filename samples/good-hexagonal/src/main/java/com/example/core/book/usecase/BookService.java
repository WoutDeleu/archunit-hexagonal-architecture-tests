package com.example.core.book.usecase;

import com.example.core.book.exceptions.BookNotFoundException;
import com.example.core.book.exceptions.DuplicateIsbnException;
import com.example.core.book.model.Book;
import com.example.core.book.model.BookId;
import com.example.core.book.port.BookRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Pure business logic service - no framework dependencies.
 * ✅ Passes: All core domain purity tests
 */
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book createBook(String title, String author, BigDecimal price, String isbn) {
        if (bookRepository.findByIsbn(isbn).isPresent()) {
            throw new DuplicateIsbnException("Book with ISBN " + isbn + " already exists");
        }

        Book book = new Book(BookId.generate(), title, author, price, isbn);
        return bookRepository.save(book);
    }

    public Book findById(BookId id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
    }

    public Book findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ISBN: " + isbn));
    }

    public List<Book> findByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public void deleteBook(BookId id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Cannot delete book - not found with ID: " + id);
        }
        bookRepository.deleteById(id);
    }
}