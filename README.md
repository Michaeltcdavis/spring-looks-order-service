# Spring Looks Order Service

The Order Service manages order creation for the Spring Looks application.
When a new order request is received, this service checks stock availability via the Inventory service and persists the order in MySQL if stock is available.

## Features

- Create orders via REST API (`POST /api/order`)
- Verify stock with external inventory endpoint (`GET /api/inventory`)
- Persist orders with Spring Data JPA + MySQL
- Manage schema changes with Flyway migrations
- Expose OpenAPI/Swagger UI for interactive API exploration
- Include resilience patterns (circuit breaker + retry) for Inventory calls
- Expose Spring Boot Actuator endpoints

## Tech Stack

- Java 21
- Spring Boot 3.4.5
- Spring Data JPA
- MySQL 8.4.5
- Flyway
- Springdoc OpenAPI 2.8.8
- Resilience4j (Circuit Breaker and Retry)
- JUnit 5, Rest Assured, WireMock, Testcontainers

## Prerequisites

- Java 21 installed
- Docker (required for local MySQL and integration tests)

## Local Setup

From the project root (`order-service`):

```bash
docker compose -p spring-looks up -d
./mvnw spring-boot:run
```

The app starts on port `8081`.

### Stop Services

```bash
docker compose -p spring-looks down
```

## API

Base URL (local): `http://localhost:8081`

### Create Order

- **Endpoint:** `POST /api/order`
- **Content-Type:** `application/json`
- **Success Status:** `201 Created`
- **Success Body:** `Order Placed Successfully`

Example request:

```bash
curl -i -X POST http://localhost:8081/api/order \
  -H "Content-Type: application/json" \
  -d '{
	"skuCode": "white_shoe",
	"price": 110,
	"quantity": 1
  }'
```

> Notes:
> - `skuCode`, `price`, and `quantity` are used by the service.
> - The Inventory service is expected at `http://localhost:8082/api/inventory` by default.

## API Documentation

- Swagger UI: `http://localhost:8081/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8081/api-docs`

## Actuator

All actuator endpoints are exposed in local configuration:

- Base: `http://localhost:8081/actuator`
- Health: `http://localhost:8081/actuator/health`

## Configuration

Main runtime properties are in `src/main/resources/application.properties`:

- `server.port=8081`
- `spring.datasource.url=jdbc:mysql://localhost:3306/order_service`
- `spring.datasource.username=root`
- `spring.datasource.password=***` (configured for local MySQL)
- `client.inventory.url=http://localhost:8082`

Database creation is bootstrapped by `docker/mysql/init.sql`:

- `order_service` database is created if missing.

Order table schema is managed by Flyway migration:

- `src/main/resources/db/migration/V1__init.sql` creates `t_orders`.

## Testing

Run tests from project root:

```bash
./mvnw test
```


