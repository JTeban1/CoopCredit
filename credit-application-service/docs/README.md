# Credit Application Service

## Overview
The `credit-application-service` is the core microservice responsible for managing credit applications in the CoopCredit system. It handles the registration, retrieval, and evaluation of credit applications, interacting with a mock risk assessment service to determine creditworthiness.

## Key Features
- **User Authentication**: Secure Login and Registration using JWT (JSON Web Tokens).
- **Credit Application Management**: 
    - Create new credit applications.
    - View applications by ID, Affiliate, or Status.
- **Risk Evaluation**: Automated credit evaluation logic integrated with external risk services.
- **Role-Based Access Control**: Granular permissions for Admin, Analyst, and Affiliate roles.

## Tech Stack
- **Languages**: Java 17
- **Framework**: Spring Boot 3.x
- **Database**: PostgreSQL
- **Security**: Spring Security + JWT
- **Migration**: Flyway
- **Mapping**: MapStruct
- **Monitoring**: Micrometer + Prometheus

## Project Structure
- `domain`: Core business logic and entities.
- `application`: Use cases, DTOs, and Ports.
- `infrastructure`: Adapters (Controllers, Persistence, Security).

## Getting Started
See [SETUP.md](./SETUP.md) for installation and running instructions.
See [API_REFERENCE.md](./API_REFERENCE.md) for detailed API documentation.
