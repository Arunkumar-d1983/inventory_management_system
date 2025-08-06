package com.inventory.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Custom exception thrown when a requested resource (e.g., product, order)
 * is not found in the system.
 *
 * Typically thrown during lookup operations such as fetching by ID.
 */
@Slf4j
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Constructs a new ResourceNotFoundException with a detailed message.
     *
     * @param message the detail message explaining which resource was not found
     */
    public ResourceNotFoundException(String message) {
        super(message);
        log.error("ResourceNotFoundException thrown: {}", message);
    }
}
