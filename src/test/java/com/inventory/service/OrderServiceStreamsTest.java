package com.inventory.service;

import com.inventory.entity.*;
import com.inventory.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceStreamsTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testOrderValueSummaryStream() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product A");
        product.setSku("SKU001");
        product.setPrice(new BigDecimal("10.00"));
        product.setStock(1);

        OrderItem item1 = new OrderItem();
        item1.setProduct(product);
        item1.setQuantity(2);

        OrderItem item2 = new OrderItem();
        item2.setProduct(product);
        item2.setQuantity(3);

        Order order = new Order();
        order.setItems(Arrays.asList(item1, item2));

        List<Order> orders = Collections.singletonList(order);
        when(orderRepository.findAll()).thenReturn(orders);

        Map<String, BigDecimal> result = orderService.summarizeTotalOrderValue(orders);

        assertEquals(new BigDecimal("50.00"), result.get("SKU001"));

    }
}
