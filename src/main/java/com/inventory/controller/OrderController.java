package com.inventory.controller;

import com.inventory.dto.OrderDTO;
import com.inventory.entity.Order;
import com.inventory.entity.OrderStatus;
import com.inventory.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

/**
 * REST controller for managing orders in the inventory system.
 */
@Validated
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Creates a new order and returns the saved order.
     *
     * @param orderDTO The DTO containing order details
     * @return ResponseEntity with the created Order
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        log.info("Fetching request to create order...");
        Order createdOrder = orderService.createOrder(orderDTO);
        log.info("Order created successfully with ID: {}", createdOrder.getId());
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    /**
     * Retrieves a list of all orders in the system.
     *
     * @return List of all Order entities
     */
    @GetMapping
    public List<Order> list() {
        log.info("Fetching all orders...");
        List<Order> orders = orderService.getAllOrders();
        log.info("Total orders fetched: {}", orders.size());
        return orders;
    }

    /**
     * Updates the status of an existing order.
     *
     * @param id     ID of the order to update
     * @param status New status to apply
     * @return Updated Order object
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        log.info("Received request to update order ID: {} with new status: {}", id, status);
        Order updatedOrder = orderService.updateStatus(id, status);
        log.info("Order ID: {} updated successfully to status: {}", updatedOrder.getId(), updatedOrder.getStatus());
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * Summarizes the total order value per product.
     *
     * @return Map of product SKU to total order value
     */
    @GetMapping("/summary")
    public Map<String, BigDecimal> getProductOrderSummary() {
        log.info("Received request to summarize total order value per product");
        Map<String, BigDecimal> summary = orderService.summarizeTotalOrderValue(orderService.getAllOrders());
        log.info("Returning order summary for {} products", summary.size());
        return summary;
    }
}
