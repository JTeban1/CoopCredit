# Architecture Documentation

## System Overview
The `credit-application-service` follows a **Hexagonal Architecture (Ports and Adapters)** to decouple business logic from external concerns like the database or API exposure.

## Layers

### 1. Domain Layer (`com.riwi.coopcredit.domain`)
- **Models**: Core entities like `CreditApplication`, `User`, `Affiliate`.
- **Ports**: Interfaces defining Inbound (Use Cases) and Outbound (Repositories/External Services) interactions.
- **Logic**: Pure Java code, no framework dependencies.

### 2. Application Layer (`com.riwi.coopcredit.application`)
- **Use Cases**: Implementation of the input ports, orchestrating the domain logic.
- **DTOs**: Data Transfer Objects for requests and responses.
- **Mappers**: Translation between DTOs and Domain Models (MapStruct).

### 3. Infrastructure Layer (`com.riwi.coopcredit.infrastructure`)
- **Adapters In**: 
    - `rest`: Spring REST Controllers exposing APIs.
- **Adapters Out**: 
    - `persistence`: Spring Data JPA repositories implementing repository ports.
    - `external`: Client for `risk-central-mock-service`.
- **Configuration**: Spring Boot Beans, Security rules, Flyway migrations.

## Data Flow
1. **Request** hits the `AuthController` or `CreditApplicationController`.
2. **Controller** uses Mappers to convert DTOs to Domain Models.
3. **Controller** calls a **Use Case** (Input Port).
4. **Use Case** executes business rules and calls **Output Ports** (Repository or External Service).
5. **Adapter Out** (e.g., JPA Repository) interacts with the Database.
6. **Result** flows back up, mapped to Response DTO, and returned to client.

## Security Architecture
- **JWT**: Stateless authentication. Token must be sent in the `Authorization: Bearer <token>` header.
- **Roles**: `ADMIN`, `ANALISTA`, `AFILIADO`.
- **Protection**: `SecurityFilterChain` disables CSRF, enables CORS, and enforces role-based access on endpoints.
