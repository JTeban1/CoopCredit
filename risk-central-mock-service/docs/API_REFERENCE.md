# API Reference

Base URL: `http://localhost:8081`

## Risk Evaluation

### Evaluate
`POST /api/risk-evaluation`

**Request Body**
```json
{
  "document": "12345678",
  "creditApplicationId": 101
}
```

**Response (200 OK)**
```json
{
  "document": "12345678",
  "creditApplicationId": 101,
  "riskScore": 750,
  "riskLevel": "LOW",
  "evaluationDetails": "Approved due to high score",
  "evaluationDate": "2024-10-27T10:00:00"
}
```

## Health Check

### Health
`GET /api/risk-evaluation/health`

**Response (200 OK)**
`Risk Central Mock Service is running`
