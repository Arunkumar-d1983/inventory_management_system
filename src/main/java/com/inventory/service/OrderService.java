package com.inventory.service;

import com.inventory.dto.OrderDTO;
import com.inventory.dto.OrderItemDTO;
import com.inventory.entity.*;
import com.inventory.exception.InsufficientStockException;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    /**
     * Creates a new order with the provided order items.
     * Ensures product stock is sufficient and updates it accordingly.
     * Transactional for consistency.
     *
     * @param orderDTO the order data transfer object
     * @return the created Order object
     * @throws IllegalArgumentException   if quantity is invalid
     * @throws ResourceNotFoundException  if the product doesn't exist
     * @throws InsufficientStockException if product stock is insufficient
     */
    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        log.info("Starting order creation for order with items: {}", orderDTO.getItems());
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDTO itemDTO : orderDTO.getItems()) {
            log.debug("Processing item: {}", itemDTO);

            if (itemDTO.getQuantity() <= 0) {
                log.error("Invalid quantity: {} for product ID: {}", itemDTO.getQuantity(), itemDTO.getProductId());
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }

            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> {
                        log.error("Product not found for ID: {}", itemDTO.getProductId());
                        return new ResourceNotFoundException("Product not found");
                    });

            if (product.getStock() < itemDTO.getQuantity()) {
                log.warn("Insufficient stock for product: {} (Available: {}, Requested: {})",
                        product.getSku(), product.getStock(), itemDTO.getQuantity());
                throw new InsufficientStockException(
                        "Insufficient stock for product: " + product.getSku());
            }

            product.setStock(product.getStock() - itemDTO.getQuantity());
            log.info("Stock updated for product: {}. Remaining: {}", product.getSku(), product.getStock());
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemDTO.getQuantity());
            orderItems.add(item);
        }
        log.info("Order Item size : {}", orderItems.size());
        // Save products (stock updated)
        try {
            productRepository.saveAll(
                    orderItems.stream()
                            .map(OrderItem::getProduct)
                            .collect(Collectors.toList()));
        } catch (OptimisticLockException e) {
            log.error("Optimistic locking failure during stock update", e);
            throw new RuntimeException("Concurrent stock update detected. Please retry.");
        }
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully with ID: {} and status: {}", savedOrder.getId(), savedOrder.getStatus());
        return savedOrder;
    }

    /**
     * Updates the status of an existing order.
     *
     * @param orderId   the order ID to update
     * @param newStatus the new status to set
     * @return the updated Order object
     * @throws ResourceNotFoundException if order is not found
     * @throws IllegalArgumentException  if status transition is invalid
     */
    @Transactional
    public Order updateStatus(Long orderId, OrderStatus newStatus) {
        log.info("Updating status for order ID: {} to {}", orderId, newStatus);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order not found with ID: {}", orderId);
                    return new ResourceNotFoundException("Order not found: " + orderId);
                });

        if (!order.getStatus().canTransitionTo(newStatus)) {
            log.warn("Invalid status transition from {} to {}", order.getStatus(), newStatus);
            throw new IllegalArgumentException("Cannot transition to same or invalid status.");
        }
        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        log.info("Order status updated successfully for ID: {} to {}", orderId, newStatus);

        return updatedOrder;
    }

    /**
     * Retrieves all orders from the database.
     *
     * @return list of all Order objects
     */
    public List<Order> getAllOrders() {
        log.info("Fetching all orders from database");
        List<Order> orders = orderRepository.findAll();
        log.info("Retrieved {} orders", orders.size());
        return orders;
    }

    /**
     * Summarizes the total order value per product SKU.
     *
     * @param orders list of orders to summarize
     * @return map of SKU to total order value
     */
    public Map<String, BigDecimal> summarizeTotalOrderValue(List<Order> orders) {
        log.info("Summarizing total order value per product SKU for {} orders", orders.size());
        Map<String, BigDecimal> summary = orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(
                        item -> item.getProduct().getSku(),
                        Collectors.mapping(
                                item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())),
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

        log.info("Order value summary computed for {} SKUs", summary.size());
        summary.forEach((sku, totalValue) -> log.debug("SKU: {}, Total Order Value: {}", sku, totalValue));
        return summary;
    }
}
