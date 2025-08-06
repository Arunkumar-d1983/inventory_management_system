# Inventory Management System API

### Project Overview
This is a **Spring Boot**-based RESTful API for managing products and orders in a retail inventory system. It supports real-time order processing, product stock management, transactional integrity, and comprehensive error handling. The API is designed with a clean architecture, object-oriented principles, and efficient stream-based data processing.

## Getting Started

#### Prerequisites:
- Ensure that JDK 1.8 or higher is installed on your machine.If Java is not installed or the version is lower than 1.8, you need to install JDK 1.8 
    * Visit the Oracle JDK download page: [Oracle JDK Download](https://www.oracle.com/java/technologies/downloads/#java8).
    * After the installation is complete, verify the Java version again using the `java -version` command to ensure that JDK 1.8
- Verify that you have Apache Maven installed. You can check by running `mvn -version` in your command line.
- Install Postman or any other API testing tool.

#### Clone the Repository
```bash
git clone https://github.com/Arunkumar-d1983/inventory-management-system.git
cd inventory-management-system
```


#### Build the application:
- Open a command and navigate to the root directory of the mbean application project.
- Run the following command to build the application.
  
Build and Run :

Build and Run (Option 1 – via Maven)
```
mvn spring-boot:run
```
(OR)

Build and Run (Option 2 – via JAR)
```
mvn clean package

After the build is successful and the JAR file is generated, navigate to the target
directory and run the following command to execute it.

cd target
java -jar inventory-management-system-0.0.1-SNAPSHOT.jar
```
#### Running Tests
To verify that the application works correctly and passes all tests, run:
``` 
mvn verify
```

## Features
- Create, retrieve, and update products and orders
- Transactional order processing with stock updates
- Handle insufficient stock with rollback
- Retrieve low-stock products using Java Streams
- Summarize total order value per product
- Input validation using Jakarta Bean Validation
- Optimistic locking for concurrency handling
- Global exception handling
- Unit testing with Mockito and JUnit

## Technology Stack
| Tool                | Description            |
|---------------------|------------------------|
| Java 8              | Programming Language   |
| Spring Boot 2.7     | Framework              |
| H2 Database         | In-memory database     |
| Spring Data JPA     | ORM Framework          |
| Lombok              | Boilerplate Reduction  |
| JUnit & Mockito     | Testing Frameworks     |
| Jakarta Validation  | Input Validation       |
| SLF4J & Logback     | Logging                |

## Project Structure

| Layer      | Components                                                                 |
|------------|----------------------------------------------------------------------------|
| **Controller** | `OrderController`,`ProductController`                                  |
| **Service**    | `OrderService`,`ProductService`                                        |
| **DTOs**       | `OrderDTO`, `OrderItemDTO`, `OrderStatusDTO`,`ProductDTO`              |
| **Entity**     | `Order`, `OrderItem`, `OrderStatus`,`Product`                          |
| **Repository** | `OrderRepository`,`ProductRepository`                                  |
| **Exception**  | `GlobalExceptionHandler`,`InsufficientStockException`, `ResourceNotFoundException` |

## Domain Model

## Product
```
- id: Long
- name: String (required)
- sku: String (unique)
- price: BigDecimal
- stock: Integer (non-negative)
```
## Order
```
- id: Long
- orderDate: LocalDateTime
- items: List<OrderItem>
- status: Enum (PENDING, COMPLETED, CANCELLED)
```
## OrderItem
```
- id: Long
- product: Product
- quantity: Integer (positive)
```


## Design Overview
➤ Design Details:
| Layer          | Components                                                 |
| -------------- | ----------------------------------------------------------- |
| `Controller`   | `OrderController, ProductController` — Expose RESTful endpoints for order and product operations.           |
| `Service`      | `OrderService, ProductService` — Contains core business logic for managing inventory and processing orders. |
| `DTOs`         | `OrderDTO, OrderItemDTO, OrderStatusDTO, ProductDTO` — Data Transfer Objects used for API requests and responses. |
| `Entity`       | `Order, OrderItem, OrderStatus, Product` — JPA entities representing the domain model.    |
| `Repository`   | `OrderRepository, ProductRepository` — Spring Data JPA interfaces for persistence operations.|
| `Exception`    | `GlobalExceptionHandler, InsufficientStockException, ResourceNotFoundException` — Handles application errors and custom exceptions.|


## API Endpoint
### 1.Create Product (POST /api/products)

Description: Creates a new product with validation.

### Example URL :
``` POST http://localhost:8080/api/products ```

### Request Body :
```json
{
  "name": "MacBook Pro",
  "sku": "MBP-14-M3",
  "price": 199999.00,
  "stock": 5
}
```
### Sample Response (201 Created) :
```json
{
    "id": 1,
    "name": "MacBook Pro",
    "sku": "MBP-14-M3",
    "price": 199999.00,
    "stock": 5,
    "version": 0
}
```
### 2.List All Products (GET /api/products)

Description: Returns all available products.

### Example URL :
``` GET http://localhost:8080/api/products ```

### Sample Response :
```json
[
    {
        "id": 1,
        "name": "MacBook Pro",
        "sku": "MBP-14-M3",
        "price": 199999.00,
        "stock": 5,
        "version": 0
    }
]
```
### 3.Get Low Stock Products (GET /api/products/low-stock?threshold=10)

Description: Uses Streams to filter products with stock below the given threshold.

### Example URL :
``` GET http://localhost:8080/api/products/low-stock?threshold=10 ```

### Sample Response :
```json
[
    {
        "id": 1,
        "name": "MacBook Pro",
        "sku": "MBP-14-M3",
        "price": 199999.00,
        "stock": 5,
        "version": 0
    }
]
```

### 4.Create Order (POST /api/orders)

Description: Creates a new order and deducts stock accordingly.

### Example URL :
``` POST http://localhost:8080/api/orders ```

### Request Body :
```json
{
  "items": [
    { "productId": 1, "quantity": 2 }
  ]
}
```
### Sample Response (201 Created) :
```json
{
    "id": 1,
    "orderDate": "2025-08-06T12:31:37.681",
    "status": "PENDING",
    "items": [
        {
            "id": 1,
            "product": {
                "id": 1,
                "name": "MacBook Pro",
                "sku": "MBP-14-M3",
                "price": 199999.00,
                "stock": 3,
                "version": 1
            },
            "quantity": 2
        }
    ]
}
```

### 5.Update Order Status (PUT /api/orders/1/status?status=COMPLETED)

Description: Updates an order’s status (e.g., to COMPLETED or CANCELLED).

### Example URL :
``` PUT http://localhost:8080/api/orders/1/status?status=COMPLETED ```

### Sample Response (200 Created) :
```json
{
    "id": 1,
    "orderDate": "2025-08-06T12:31:37.681",
    "status": "COMPLETED",
    "items": [
        {
            "id": 1,
            "product": {
                "id": 1,
                "name": "MacBook Pro",
                "sku": "MBP-14-M3",
                "price": 199999.00,
                "stock": 3,
                "version": 1
            },
            "quantity": 2
        }
    ]
}
```

### 6.Get Low Stock Products (GET /api/orders/summary)

Description: Returns total order value per product using Java Streams.

### Example URL :
``` GET http://localhost:8080/api/orders/summary ```

### Sample Response :
```json
{
    "MBP-14-M3": 399998.00
}
```

## Logging Configuration

Logging is configured in application.properties:

### application.properties
```
logging.level.root=INFO
logging.level.com.inventory=INFO
logging.pattern.console=
logging.file.name=logs/inventory-management.log
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
```

- Console logs are disabled.
- All logs are written to logs/inventory-management-app.log
- Debug logs include transaction, controller, and service flow.

All logs will be written to logs/inventory-management-app.log. Console output is disabled.

