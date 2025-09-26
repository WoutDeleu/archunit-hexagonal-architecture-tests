package com.example.ecommerce.adapters.database.adapter;

import com.example.ecommerce.adapters.database.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaProductRepository extends JpaRepository<ProductEntity, UUID> {
    @Query("SELECT p FROM ProductEntity p WHERE p.name LIKE %:name%")
    List<ProductEntity> findByNameContaining(@Param("name") String name);
}