package com.inventory.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing an order placed by a customer.
 * It encapsulates the list of items included in the order.
 * Used for creating and processing new orders in the inventory system.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    /**
     * The list of individual items included in the order.
     * Each item contains product details such as product ID, quantity, and price.
     * 
     * <p>
     * This field must not be null and should contain at least one valid
     * {@link OrderItemDTO}.
     * </p>
     */
    @NotNull(message = "Order items cannot be null")
    private List<OrderItemDTO> items;

}
