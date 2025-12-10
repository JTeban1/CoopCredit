# CoopCredit - Microservices Credit Application System

A multi-module Spring Boot 3.5 microservices system for credit application management, built with Hexagonal Architecture.

## 📋 Table of Contents

- [Architecture](#architecture)
- [Modules](#modules)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Docker Deployment](#docker-deployment)
- [Configuration](#configuration)

## 🏗 Architecture

This project follows **Hexagonal Architecture** (Ports and Adapters) for the main service:

```
credit-application-service/
├── domain/           # Pure business logic (no frameworks)
│   ├── model/        # Domain POJOs
│   ├── port/in/      # Input ports (use cases)
│   ├── port/out/     # Output ports (repository interfaces)
│   └── service/      # Use case implementations
├── application/      # Application layer
│   ├── dto/          # Request/Response DTOs
│   └── mapper/       # Domain ↔ DTO mappers
└── infrastructure/   # Adapters
    ├── adapter/in/   # REST Controllers
    ├── adapter/out/  # JPA, External services
    ├── config/       # Spring configuration
    ├── security/     # JWT, Authentication
    └── exception/    # Error handling
```


## Frontend Architecture

The frontend is built using **React** with **TypeScript** and **Vite**. Key libraries include:

- **TailwindCSS**: For utility-first styling.
- **React Hook Form** & **Zod**: For robust form handling and validation.
- **React Router**: For client-side routing.
- **Axios**: For API integration with the backend services.
- **Lucide React**: For modern iconography.

```
coopcredit-frontend/
├── src/
│   ├── api/          # Axios client and API calls
│   ├── components/   # Reusable UI components
│   ├── contexts/     # React Context (Auth, etc.)
│   ├── pages/        # Page components
│   ├── types/        # TypeScript interfaces
│   └── utils/        # Helpers and validators
└── public/           # Static assets
```

## 📦 Modules

| Module | Description | Port |
|--------|-------------|------|
| `credit-application-service` | Main microservice for credit management | 8080 |
| `risk-central-mock-service` | Mock external risk evaluation service | 8081 |
| `coopcredit-frontend` | React Frontend Application | 80 (Docker) / 5173 (Dev) |

## ⚙️ Prerequisites

- Java 17+
- Maven 3.9+
- Docker & Docker Compose (for containerized deployment)
- PostgreSQL 15+ (for local development without Docker)

## 🚀 Quick Start

### Using Docker Compose (Recommended)

```bash
# Clone the repository
git clone <repository-url>
cd CoopCredit

# Build and start all services
docker-compose up --build

# Or run in detached mode
docker-compose up -d --build

# View logs
docker-compose logs -f

# Stop services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

### Local Development

1. **Start PostgreSQL database:**
```bash
# Create database
psql -U postgres -c "CREATE DATABASE coopcredit;"
```

2. **Start risk-central-mock-service:**
```bash
cd risk-central-mock-service
mvn spring-boot:run
```

3. **Start credit-application-service:**
```bash
cd credit-application-service
mvn spring-boot:run
```

## 📡 API Documentation

### Authentication Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | Register new user with affiliate |
| POST | `/auth/login` | Login and get JWT token |

### Affiliate Endpoints

| Method | Endpoint | Description | Roles |
|--------|----------|-------------|-------|
| POST | `/api/affiliates` | Register affiliate | ADMIN, ANALISTA |
| GET | `/api/affiliates/{id}` | Get affiliate by ID | ALL |
| GET | `/api/affiliates/document/{doc}` | Get by document | ADMIN, ANALISTA |

### Credit Application Endpoints

| Method | Endpoint | Description | Roles |
|--------|----------|-------------|-------|
| POST | `/api/applications` | Create application | ALL |
| GET | `/api/applications/{id}` | Get application | ALL |
| GET | `/api/applications/affiliate/{id}` | Get by affiliate | ALL |
| POST | `/api/applications/{id}/evaluate` | Evaluate risk | ADMIN, ANALISTA |
| GET | `/api/applications/status/{status}` | Get by status | ADMIN, ANALISTA |

### Example Requests

**Register User:**
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe",
    "password": "password123",
    "email": "john@example.com",
    "affiliate": {
      "document": "123456789",
      "firstName": "John",
      "lastName": "Doe",
      "email": "john@example.com",
      "phone": "3001234567",
      "salary": 3000000
    }
  }'
```

**Login:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe",
    "password": "password123"
  }'
```

**Create Credit Application:**
```bash
curl -X POST http://localhost:8080/api/applications \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "affiliateId": 1,
    "requestedAmount": 5000000,
    "termMonths": 24,
    "purpose": "Home improvement"
  }'
```

## 🧪 Testing

```bash
# Run all tests
mvn clean test

# Run specific module tests
cd credit-application-service
mvn clean test

# Run integration tests (requires Docker)
mvn clean verify
```

## 🐳 Docker Deployment

### Build Images

```bash
# Build all images
docker-compose build

# Build specific service
docker-compose build credit-application-service
```

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_URL` | `jdbc:postgresql://localhost:5432/coopcredit` | Database URL |
| `DB_USER` | `postgres` | Database username |
| `DB_PASSWORD` | `postgres` | Database password |
| `JWT_SECRET` | (required) | JWT signing key (min 64 chars) |
| `JWT_EXPIRATION_MINUTES` | `1440` | Token expiration time |
| `RISK_CENTRAL_URL` | `http://localhost:8081` | Risk service URL |

### Frontend Environment Variables
| Variable | Default | Description |
|----------|---------|-------------|
| `VITE_API_BASE_URL` | `http://localhost:8080` | Backend API URL |

## 📊 Monitoring

- **Health:** http://localhost:8080/actuator/health
- **Metrics:** http://localhost:8080/actuator/metrics
- **Prometheus:** http://localhost:8080/actuator/prometheus

## 🔐 Security

- JWT-based stateless authentication
- BCrypt password encoding
- Role-based access control (ROLE_AFILIADO, ROLE_ANALISTA, ROLE_ADMIN)
- Default admin credentials: `admin` / `admin123`

## 📝 License

This project is licensed under the MIT License.
