package com.inventory.dto;

import com.inventory.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) representing the status update for an order.
 * This class is typically used when updating the status of an existing order
 * in the system (e.g., marking it as COMPLETED or CANCELLED).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusDTO {
    /**
     * The current status of the order.
     * This field must not be null and typically takes values from the
     * {@link OrderStatus} enum,
     * such as PENDING, COMPLETED, or CANCELLED.
     */
    @NotNull(message = "Order status cannot be null")
    private OrderStatus status;
}
