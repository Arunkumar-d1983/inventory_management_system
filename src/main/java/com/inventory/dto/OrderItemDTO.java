package com.inventory.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing a single item within an order.
 * This includes the product ID being ordered and the quantity requested.
 * Used when creating or updating orders in the inventory management system.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    /**
     * Unique identifier of the product being ordered.
     * This must refer to an existing product in the catalog.
     * Cannot be null.
     * 
     * Example: 1001L
     */
    @NotNull(message = "Product ID must not be null.")
    private Long productId;

    /**
     * The number of units of the product being ordered.
     * Must be at least 1 to ensure valid order creation.
     * 
     * Example: 2
     */
    @Min(value = 1, message = "Quantity must be at least 1.")
    private Integer quantity;

}
