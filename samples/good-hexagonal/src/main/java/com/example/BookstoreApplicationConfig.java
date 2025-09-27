package com.example;

import com.example.core.book.port.BookRepository;
import com.example.core.book.usecase.BookService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class - correctly placed in infrastructure.
 * ✅ Passes: infrastructure_should_only_contain_config_and_utils
 * ✅ Passes: infrastructure_should_not_contain_business_logic
 */
@Configuration
public class BookstoreApplicationConfig {

    @Bean
    public BookService bookService(BookRepository bookRepository) {
        return new BookService(bookRepository);
    }
}