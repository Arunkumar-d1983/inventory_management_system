package com.inventory.stream;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import com.inventory.entity.OrderItem;
import com.inventory.entity.Product;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StreamLogicTest {

        @Test
        void testLowStockFilter() {
                List<Product> products = Arrays.asList(
                                new Product(null, "A", "SKU1", BigDecimal.TEN, 3, null),
                                new Product(null, "B", "SKU2", BigDecimal.TEN, 8, null),
                                new Product(null, "C", "SKU3", BigDecimal.TEN, 12, null));

                List<Product> lowStock = products.stream()
                                .filter(p -> p.getStock() < 10)
                                .collect(Collectors.toList());

                assertEquals(2, lowStock.size());
        }

        @Test
        void testTotalOrderValuePerProduct() {
                Product p1 = new Product(null, "A", "SKU1", BigDecimal.valueOf(10), 10, null);
                Product p2 = new Product(null, "B", "SKU2", BigDecimal.valueOf(20), 10, null);

                OrderItem i1 = new OrderItem(null, p1, 2, null);
                OrderItem i2 = new OrderItem(null, p2, 3, null);
                OrderItem i3 = new OrderItem(null, p1, 1, null);

                List<OrderItem> allItems = Arrays.asList(i1, i2, i3);

                Map<Product, BigDecimal> summary = allItems.stream()
                                .collect(Collectors.groupingBy(
                                                OrderItem::getProduct,
                                                Collectors.mapping(
                                                                i -> i.getProduct().getPrice().multiply(
                                                                                BigDecimal.valueOf(i.getQuantity())),
                                                                Collectors.reducing(BigDecimal.ZERO,
                                                                                BigDecimal::add))));

                assertEquals(BigDecimal.valueOf(30), summary.get(p1));
                assertEquals(BigDecimal.valueOf(60), summary.get(p2));
        }
}
