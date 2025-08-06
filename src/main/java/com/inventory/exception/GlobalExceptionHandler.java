package com.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles ResourceNotFoundException and returns a NOT_FOUND (404) response.
     *
     * @param ex the ResourceNotFoundException thrown
     * @return ResponseEntity with error message and 404 status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles InsufficientStockException and returns a BAD_REQUEST (400) response.
     *
     * @param ex the InsufficientStockException thrown
     * @return ResponseEntity with error message and 400 status
     */
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<String> handleStock(InsufficientStockException ex) {
        log.warn("Insufficient stock error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles IllegalArgumentException and returns a BAD_REQUEST (400) response.
     *
     * @param ex the IllegalArgumentException thrown
     * @return ResponseEntity with error message and 400 status
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        log.warn("Bad request due to invalid input: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles validation errors caused by invalid method arguments annotated
     * with @Valid.
     *
     * @param ex the MethodArgumentNotValidException thrown during request body
     *           validation.
     * @return ResponseEntity containing a brief validation error message and HTTP
     *         status 400.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body("Invalid input: " + ex.getMessage());
    }

    /**
     * Handles optimistic locking failures caused by concurrent updates on versioned
     * entities.
     *
     * @param ex the ObjectOptimisticLockingFailureException thrown by Spring JPA.
     * @return ResponseEntity with a message suggesting a retry and HTTP status 409
     *         (Conflict).
     */
    @ExceptionHandler(org.springframework.orm.ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<String> handleOptimisticLockingFailure(ObjectOptimisticLockingFailureException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Concurrent update error. Please retry the transaction.");
    }

    /**
     * Handles all uncaught exceptions and returns a generic INTERNAL_SERVER_ERROR
     * (500) response.
     *
     * @param ex the Exception thrown
     * @return ResponseEntity with a generic error message and 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception ex) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}