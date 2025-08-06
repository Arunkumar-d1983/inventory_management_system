package com.inventory.repository;

import com.inventory.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing and managing Product entities.
 * Extends JpaRepository to provide CRUD and pagination capabilities.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);

    List<Product> findByStockLessThan(int threshold);
}
