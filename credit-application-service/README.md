# Credit Application Service

Main microservice for credit application management in the CoopCredit system.

## Architecture

This service follows **Hexagonal Architecture** (Ports and Adapters):

- **Domain Layer**: Pure business logic with no framework dependencies
- **Application Layer**: DTOs and mappers for data transformation
- **Infrastructure Layer**: Spring Boot adapters, security, and configuration

## Features

- ✅ Affiliate registration and management
- ✅ Credit application submission
- ✅ Business rule validation (seniority, amount limits, installment ratio)
- ✅ Integration with external risk evaluation service
- ✅ JWT-based authentication
- ✅ Role-based authorization
- ✅ Prometheus metrics
- ✅ Database migrations with Flyway

## Tech Stack

- Java 17
- Spring Boot 3.5
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Flyway
- Testcontainers

## Running Locally

```bash
# Ensure PostgreSQL is running and coopcredit database exists
mvn spring-boot:run

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Testing

```bash
# Unit tests
mvn test

# Integration tests (requires Docker for Testcontainers)
mvn verify
```

## API Endpoints

See main project README for complete API documentation.

## Configuration

Key properties in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/coopcredit
    
security:
  jwt:
    secret: your-secret-key
    expiration-minutes: 1440

risk-central:
  url: http://localhost:8081
```
