package com.example.ecommerce.violations;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.stereotype.Repository;
import java.util.UUID;

// VIOLATION: JPA Entity with Spring annotations
@Entity
@Repository  // This should cause a violation
public class BadEntityWithSpringAnnotations {
    @Id
    private UUID id;
    private String name;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}