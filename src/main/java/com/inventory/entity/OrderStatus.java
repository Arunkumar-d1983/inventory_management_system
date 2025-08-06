package com.inventory.entity;

import lombok.extern.slf4j.Slf4j;

/**
 * Enum representing the possible statuses of an order in the system.
 * 
 * PENDING - Order has been created but not yet processed.
 * COMPLETED - Order has been successfully fulfilled.
 * CANCELLED - Order has been cancelled and will not be processed.
 */
@Slf4j
public enum OrderStatus {
    PENDING, COMPLETED, CANCELLED;

    /**
     * Determines whether an order can transition from the current status
     * to the specified new status.
     *
     * @param newStatus The desired status to transition to.
     * @return true if the transition is allowed, false otherwise.
     */
    public boolean canTransitionTo(OrderStatus newStatus) {

        log.info("Attempting status transition from {} to {}", this, newStatus);

        // COMPLETED orders are final and cannot transition further
        if (this == COMPLETED) {
            log.warn("Transition denied: Order is already COMPLETED.");
            return false;
        }

        // CANCELLED orders are final and cannot transition further
        if (this == CANCELLED) {
            log.warn("Transition denied: Order is already CANCELLED.");
            return false;
        }

        // Transitioning to the same state is not considered a valid transition
        if (this == newStatus) {
            log.warn("Transition denied: Order is already in {} status.", this);
            return false;
        }

        log.info("Transition allowed: {} to {}", this, newStatus);
        return true;
    }
}
