package com.inventory.controller;

import com.inventory.dto.ProductDTO;
import com.inventory.entity.Product;
import com.inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.util.List;

/**
 * REST controller for managing products in the inventory system.
 */
@Validated
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Creates a new product in the inventory.
     *
     * @param dto The product data transfer object containing product details
     * @return The created product
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        log.info("Received request to create new product with SKU: {}", productDTO.getSku());
        Product createdProduct = productService.createProduct(productDTO);
        log.info("Product created successfully with ID: {}", createdProduct.getId());
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    /**
     * Retrieves the list of all available products.
     *
     * @return List of products
     */
    @GetMapping
    public List<Product> list() {
        log.info("Received request to list all products");
        List<Product> productList = productService.listAll();
        log.info("Returning {} products", productList.size());
        return productList;
    }

    /**
     * Retrieves products with stock below the specified threshold.
     *
     * @param threshold The stock threshold value
     * @return List of low-stock products
     */
    @GetMapping("/low-stock")
    public List<Product> getLowStock(@RequestParam int threshold) {
        log.info("Received request to get products with stock below threshold: {}", threshold);
        List<Product> lowStockProducts = productService.getLowStockProducts(threshold);
        log.info("Found {} products below stock threshold of {}", lowStockProducts.size(), threshold);
        return lowStockProducts;
    }

}