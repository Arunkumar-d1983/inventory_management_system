package com.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootTest
@ComponentScan(basePackages = "com.inventory")
@EnableJpaRepositories(basePackages = "com.inventory.repository")
@EntityScan(basePackages = "com.inventory.entity")
class InventoryManagementSystemApplicationTests {

	@Test
	void contextLoads() {
	}

}
