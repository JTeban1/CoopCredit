# Risk Central Mock Service

Mock external service that simulates a credit risk evaluation system.

## Purpose

This service provides a mock implementation of an external risk evaluation API for testing and development purposes. It generates deterministic risk scores based on the applicant's document number.

## Endpoints

### Evaluate Risk

```
POST /api/risk-evaluation
```

**Request Body:**
```json
{
  "document": "123456789",
  "creditApplicationId": 1
}
```

**Response:**
```json
{
  "document": "123456789",
  "creditApplicationId": 1,
  "riskScore": 650,
  "riskLevel": "MEDIUM",
  "evaluationDetails": "Risk evaluation completed. Score: 650, Level: MEDIUM...",
  "evaluationDate": "2024-01-15T10:30:00"
}
```

### Risk Levels

| Score Range | Level |
|-------------|-------|
| 700 - 900 | LOW |
| 500 - 699 | MEDIUM |
| 300 - 499 | HIGH |

## Running

```bash
mvn spring-boot:run
```

The service starts on port 8081 by default.

## Health Check

```
GET /actuator/health
```

## Configuration

```yaml
server:
  port: 8081
```
