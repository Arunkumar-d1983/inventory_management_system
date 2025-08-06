package com.inventory.controller;

import com.inventory.dto.ProductDTO;
import com.inventory.entity.Product;
import com.inventory.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.Collections;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testCreateProductSuccess() throws Exception {
        ProductDTO dto = new ProductDTO();
        dto.setName("Test Product");
        dto.setSku("SKU001");
        dto.setPrice(new BigDecimal("10.00"));
        dto.setStock(100);

        Product product = new Product();
        product.setId(1L);
        product.setName(dto.getName());
        product.setSku(dto.getSku());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());

        Mockito.when(productService.createProduct(Mockito.any())).thenReturn(product);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    public void testLowStockProducts() throws Exception {
        Product lowStock = new Product();
        lowStock.setId(1L);
        lowStock.setName("Low Stock Product");
        lowStock.setSku("SKU001");
        lowStock.setPrice(new BigDecimal("10.00"));
        lowStock.setStock(3);

        Mockito.when(productService.getLowStockProducts(5))
                .thenReturn(Collections.singletonList(lowStock));

        mockMvc.perform(get("/api/products/low-stock")
                .param("threshold", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Low Stock Product"));
    }
}