package com.example.adapters.database.adapter;

import com.example.adapters.database.entity.BookEntity;
import com.example.core.book.model.Book;
import com.example.core.book.model.BookId;
import com.example.core.book.port.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository adapter implementing core port - correctly placed and annotated.
 * ✅ Passes: repositories_should_be_in_database_adapters
 * ✅ Passes: repositories_should_not_be_in_core
 * ✅ Passes: database_adapters_should_implement_core_interfaces
 * ✅ Passes: adapters_should_implement_core_interfaces
 */
@Service
public class BookRepositoryAdapter implements BookRepository {
    private final JpaBookRepository jpaRepository;

    public BookRepositoryAdapter(JpaBookRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Book save(Book book) {
        BookEntity entity = BookEntity.fromDomain(book);
        BookEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Book> findById(BookId id) {
        return jpaRepository.findById(id.getValue())
                .map(BookEntity::toDomain);
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return jpaRepository.findByIsbn(isbn)
                .map(BookEntity::toDomain);
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return jpaRepository.findByAuthor(author)
                .stream()
                .map(BookEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(BookEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(BookId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsById(BookId id) {
        return jpaRepository.existsById(id.getValue());
    }
}