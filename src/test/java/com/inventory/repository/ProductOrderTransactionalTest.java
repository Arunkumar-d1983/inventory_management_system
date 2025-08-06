package com.inventory.repository;

import com.inventory.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductOrderTransactionalTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    public void testRollbackOnFailure() {
        // Arrange
        Product product = new Product();
        product.setName("Test");
        product.setSku("TEST123");
        product.setPrice(new BigDecimal("100.0"));
        product.setStock(5);
        productRepository.save(product);

        try {
            product.setStock(product.getStock() - 10);
            productRepository.save(product);
            throw new IllegalArgumentException("Forcing rollback");
        } catch (IllegalArgumentException ignored) {
        }

        entityManager.clear();

        Product updatedProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new NoSuchElementException("Not found"));

        assertEquals(5, updatedProduct.getStock());
    }
}