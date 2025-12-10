# API Reference

Base URL: `http://localhost:8080`

## Authentication

### Login
`POST /auth/login`

**Request Body**
```json
{
  "username": "jdoe",
  "password": "password123"
}
```

**Response (200 OK)**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "jdoe",
  "email": "jdoe@example.com",
  "roles": ["ROLE_AFILIADO"],
  "affiliateId": 1
}
```

### Register
`POST /auth/register`

**Request Body**
```json
{
  "username": "jdoe",
  "password": "password123",
  "email": "jdoe@example.com",
  "affiliate": {
      "name": "John Doe",
      "Address": "123 Main St",
      "phone": "555-0199"
  }
}
```

**Response (201 Created)**
Same as Login response.

## Credit Applications

### Create Application
`POST /api/applications`
*Requires Role: ADMIN, ANALISTA, AFILIADO*

**Headers**
`Authorization: Bearer <token>`

**Request Body**
```json
{
  "affiliateId": 1,
  "amount": 5000.00,
  "term": 12,
  "purpose": "Home Improvement"
}
```

**Response (201 Created)**
```json
{
  "id": 101,
  "affiliateId": 1,
  "amount": 5000.00,
  "term": 12,
  "status": "PENDING",
  "createdAt": "2024-10-27T10:00:00"
}
```

### Get Application by ID
`GET /api/applications/{id}`
*Requires Role: ADMIN, ANALISTA, AFILIADO*

**Response (200 OK)**
Returns the application object.

### Evaluate Application
`POST /api/applications/{id}/evaluate`
*Requires Role: ADMIN, ANALISTA*

**Response (200 OK)**
```json
{
  "id": 101,
  "status": "APPROVED",
  "riskScore": 750,
  "updatedAt": "2024-10-27T10:05:00"
}
```

### List by Status
`GET /api/applications/status/{status}`
*Requires Role: ADMIN, ANALISTA*

**Response (200 OK)**
Returns a list of applications.
