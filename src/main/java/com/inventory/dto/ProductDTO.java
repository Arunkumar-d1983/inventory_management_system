package com.inventory.dto;

import java.math.BigDecimal;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing product details in the inventory
 * system.
 * This class is used to transfer data between layers (e.g., controller to
 * service)
 * and includes validation annotations to ensure data integrity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    /**
     * Name of the product.
     * This field is required and cannot be blank.
     * <p>
     * Example: "Wireless Mouse"
     * </p>
     */
    @NotBlank(message = "Product name must not be blank.")
    private String name;

    /**
     * Unique Stock Keeping Unit (SKU) identifier for the product.
     * This helps uniquely identify products in the inventory.
     * <p>
     * Example: "WM12345"
     * </p>
     */
    @NotBlank(message = "SKU must not be blank.")
    private String sku;

    /**
     * Price of the product.
     * Must be a non-null value and zero or greater.
     * <p>
     * Example: 29.99
     * </p>
     */
    @NotNull(message = "Price must not be null.")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be greater than or equal to 0.0.")
    private BigDecimal price;

    /**
     * Number of units available in stock.
     * Must be a non-null integer and cannot be negative.
     * <p>
     * Example: 150
     * </p>
     */
    @NotNull(message = "Stock must not be null.")
    @Min(value = 0, message = "Stock must be greater than or equal to 0.")
    private Integer stock;
}
