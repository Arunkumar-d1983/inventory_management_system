package com.inventory.service;

import com.inventory.dto.ProductDTO;
import com.inventory.entity.Product;
import com.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Creates a new product with validation for SKU uniqueness and non-negative
     * stock.
     *
     * @param productDTO the data transfer object containing product details
     * @return the saved Product entity
     * @throws IllegalArgumentException if stock is negative or SKU already exists
     */
    @Transactional
    public Product createProduct(ProductDTO productDTO) {
        log.info("Attempting to create product with SKU: {}", productDTO.getSku());

        if (productDTO.getStock() < 0) {
            log.error("Invalid stock value: {} for SKU: {}", productDTO.getStock(), productDTO.getSku());
            throw new IllegalArgumentException("Stock cannot be negative");
        }

        if (productRepository.findBySku(productDTO.getSku()).isPresent()) {
            log.warn("Duplicate SKU detected: {}", productDTO.getSku());
            throw new IllegalArgumentException("SKU must be unique");
        }
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setSku(productDTO.getSku());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with ID: {} and SKU: {}", savedProduct.getId(), savedProduct.getSku());

        return savedProduct;
    }

    /**
     * Lists all products in the inventory.
     *
     * @return list of all products
     */
    public List<Product> listAll() {
        log.info("Fetching all products from the repository.");
        List<Product> products = productRepository.findAll();
        log.info("Total products found: {}", products.size());
        return products;
    }

    /**
     * Retrieves all products with stock below the specified threshold.
     *
     * @param threshold stock value to compare
     * @return list of products with stock less than the threshold
     */
    public List<Product> getLowStockProducts(int threshold) {
        log.info("Fetching products with stock below threshold: {}", threshold);
        List<Product> lowStockProducts = productRepository.findAll().stream()
                .filter(p -> p.getStock() < threshold)
                .collect(Collectors.toList());
        log.info("Low stock products found: {}", lowStockProducts.size());
        return lowStockProducts;
    }

}
