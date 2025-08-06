package com.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.inventory.entity.Order;

/**
 * Repository interface for accessing and managing Order entities.
 * Extends JpaRepository to provide CRUD and pagination capabilities.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
