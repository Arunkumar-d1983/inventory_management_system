package com.inventory.repository;

import com.inventory.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class OptimisticLockingTest {

    @Autowired
    private ProductRepository productRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testOptimisticLockingFailure() {
        // Step 1: Create and save a product
        Product product = new Product();
        product.setName("ConcurrentProduct");
        product.setSku("SKU-LOCK-01");
        product.setPrice(BigDecimal.valueOf(100.00));
        product.setStock(10);
        productRepository.saveAndFlush(product);

        // Step 2: Load same entity in two different contexts (simulate concurrency)
        Product p1 = entityManager.find(Product.class, product.getId());
        entityManager.detach(p1); // detach from persistence context

        Product p2 = entityManager.find(Product.class, product.getId());
        entityManager.detach(p2);

        // Step 3: Modify and save first instance
        p1.setStock(9);
        productRepository.saveAndFlush(p1); // This will increment version

        // Step 4: Modify and save second instance (stale version)
        p2.setStock(8);
        // Expect version conflict
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            productRepository.saveAndFlush(p2);
        });
    }
}
