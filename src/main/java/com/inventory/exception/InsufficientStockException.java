package com.inventory.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Custom exception thrown when attempting to perform an operation
 * that exceeds available stock for a product.
 *
 * Typically thrown during order creation or stock reduction.
 */
@Slf4j
public class InsufficientStockException extends RuntimeException {
    /**
     * Constructs a new InsufficientStockException with a detailed message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public InsufficientStockException(String message) {
        super(message);
        log.error("InsufficientStockException thrown: {}", message);
    }
}
