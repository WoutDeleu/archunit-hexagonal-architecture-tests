package com.example.core.user;

import org.springframework.stereotype.Component;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * ❌ VIOLATION 1: Core entity using Spring annotations
 * ❌ Fails: core_should_not_use_spring_annotations
 *
 * ❌ VIOLATION 2: Core entity using JPA annotations
 * ❌ Fails: core_should_not_use_jpa_annotations
 *
 * ❌ VIOLATION 3: Core depending on JPA
 * ❌ Fails: core_should_not_depend_on_jpa
 *
 * ❌ VIOLATION 4: Core depending on Spring framework
 * ❌ Fails: core_should_not_depend_on_spring_framework
 */
@Component  // ❌ Spring annotation in core!
@Entity     // ❌ JPA annotation in core!
@Table(name = "users")  // ❌ JPA annotation in core!
public class User {
    @Id  // ❌ JPA annotation in core!
    private Long id;
    private String name;
    private String email;

    // Constructors, getters, setters...
    public User() {}

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}