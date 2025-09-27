package com.example.adapters.database.adapter;

import com.example.adapters.database.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository - correctly placed in database adapter.
 * ✅ Passes: repositories_should_be_in_database_adapters
 * ✅ Passes: repositories_should_not_be_in_core
 */
@Repository
public interface JpaBookRepository extends JpaRepository<BookEntity, UUID> {

    Optional<BookEntity> findByIsbn(String isbn);

    List<BookEntity> findByAuthor(String author);
}