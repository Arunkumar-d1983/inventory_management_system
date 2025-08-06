package com.inventory.service;

import com.inventory.dto.OrderDTO;
import com.inventory.dto.OrderItemDTO;
import com.inventory.entity.*;
import com.inventory.exception.InsufficientStockException;
import com.inventory.repository.OrderRepository;
import com.inventory.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrderSuccess() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Item");
        product.setSku("SKU001");
        product.setPrice(new BigDecimal("50.00"));
        product.setStock(10);

        OrderItemDTO itemDTO = new OrderItemDTO();
        itemDTO.setProductId(1L);
        itemDTO.setQuantity(2);

        OrderDTO dto = new OrderDTO();
        dto.setItems(Collections.singletonList(itemDTO));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        Order result = orderService.createOrder(dto);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        verify(productRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testCreateOrder_InsufficientStock() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Item");
        product.setSku("SKU001");
        product.setPrice(new BigDecimal("50.00"));
        product.setStock(1);

        OrderItemDTO itemDTO = new OrderItemDTO();
        itemDTO.setProductId(1L);
        itemDTO.setQuantity(5);

        OrderDTO dto = new OrderDTO();
        dto.setItems(Collections.singletonList(itemDTO));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Exception exception = assertThrows(InsufficientStockException.class, () -> {
            orderService.createOrder(dto);
        });
        assertTrue(exception.getMessage().contains("Insufficient stock"));
        verify(orderRepository, times(0)).save(any(Order.class));
    }
}