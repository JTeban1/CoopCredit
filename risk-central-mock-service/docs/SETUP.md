# Setup Guide

## Prerequisites
- **Java 17**+
- **Maven**

## Configuration
The service runs on port `8081` by default to avoid conflict with the main service.

| Variable | Description | Default |
|----------|-------------|---------|
| `SERVER_PORT` | Port to run on | `8081` |

## Running the Service

1. **Build**:
   ```bash
   ./mvnw clean package
   ```

2. **Run**:
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Verify**:
   - Health: `http://localhost:8081/actuator/health`
   - Test: `curl -X POST http://localhost:8081/api/risk-evaluation -H "Content-Type: application/json" -d '{"document":"12345", "creditApplicationId":1}'`
