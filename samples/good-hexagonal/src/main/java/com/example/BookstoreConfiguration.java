package com.example;

import com.example.core.book.port.BookRepository;
import com.example.core.book.usecase.BookService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Application configuration class - correctly placed at root level next to main application.
 * ✅ Demonstrates: Proper placement of configuration that creates core beans
 * ✅ Follows hexagonal architecture: Configuration at application boundary, not in infrastructure
 */
@Configuration
public class BookstoreConfiguration {

    @Bean
    public BookService bookService(BookRepository bookRepository) {
        return new BookService(bookRepository);
    }
}