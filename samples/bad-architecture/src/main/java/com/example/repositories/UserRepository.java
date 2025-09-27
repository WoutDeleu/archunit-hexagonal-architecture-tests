package com.example.repositories;

import com.example.core.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ❌ VIOLATION 1: Repository NOT in adapters.database package
 * ❌ Fails: repositories_should_be_in_database_adapters
 *
 * ❌ VIOLATION 2: Repository NOT implementing a core port interface
 * ❌ Fails: database_adapters_should_implement_core_interfaces
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}