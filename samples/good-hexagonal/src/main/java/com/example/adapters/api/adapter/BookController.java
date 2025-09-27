package com.example.adapters.api.adapter;

import com.example.adapters.api.entity.BookDto;
import com.example.adapters.api.entity.CreateBookRequest;
import com.example.core.book.model.Book;
import com.example.core.book.model.BookId;
import com.example.core.book.port.BookCommandHandler;
import com.example.core.book.port.BookQueryHandler;
import com.example.core.book.usecase.BookService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller implementing core input ports - perfect hexagonal architecture.
 * ✅ Passes: controllers_should_be_in_api_adapters
 * ✅ Passes: controllers_should_not_be_in_core
 * ✅ Passes: controllers_should_use_core_port_interfaces
 * ✅ Passes: adapters_should_implement_core_interfaces
 */
@RestController
@RequestMapping("/api/books")
public class BookController implements BookCommandHandler, BookQueryHandler {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // REST endpoints delegating to port interface methods

    @PostMapping
    public BookDto createBookEndpoint(@RequestBody CreateBookRequest request) {
        BookCommandHandler.CreateBookCommand command = new BookCommandHandler.CreateBookCommand(
                request.getTitle(),
                request.getAuthor(),
                request.getPrice().toString(),
                request.getIsbn()
        );
        Book book = createBook(command);
        return BookDto.fromDomain(book);
    }

    @GetMapping("/{id}")
    public BookDto getBookEndpoint(@PathVariable String id) {
        Book book = getBookById(id);
        return BookDto.fromDomain(book);
    }

    @GetMapping("/isbn/{isbn}")
    public BookDto getBookByIsbnEndpoint(@PathVariable String isbn) {
        Book book = getBookByIsbn(isbn);
        return BookDto.fromDomain(book);
    }

    @GetMapping("/author/{author}")
    public List<BookDto> getBooksByAuthorEndpoint(@PathVariable String author) {
        return getBooksByAuthor(author)
                .stream()
                .map(BookDto::fromDomain)
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<BookDto> getAllBooksEndpoint() {
        return getAllBooks()
                .stream()
                .map(BookDto::fromDomain)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void deleteBookEndpoint(@PathVariable String id) {
        deleteBook(id);
    }

    // Implementation of BookCommandHandler port interface

    @Override
    public Book createBook(CreateBookCommand command) {
        return bookService.createBook(
                command.title(),
                command.author(),
                new BigDecimal(command.price()),
                command.isbn()
        );
    }

    @Override
    public Book getBookById(String id) {
        return bookService.findById(BookId.from(id));
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        return bookService.findByIsbn(isbn);
    }

    @Override
    public void deleteBook(String id) {
        bookService.deleteBook(BookId.from(id));
    }

    // Implementation of BookQueryHandler port interface

    @Override
    public List<Book> getAllBooks() {
        return bookService.findAllBooks();
    }

    @Override
    public List<Book> getBooksByAuthor(String author) {
        return bookService.findByAuthor(author);
    }
}