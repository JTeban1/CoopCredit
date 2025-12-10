# Requirements Documentation

## Functional Requirements

### 1. Authentication & Authorization
- **Register**: Users must be able to register as an Affiliate.
- **Login**: Users must log in with username/password to receive a JWT.
- **Roles**:
    - `AFILIADO`: Can create and view their own applications.
    - `ANALISTA`: Can view all applications and evaluate them.
    - `ADMIN`: Full access.

### 2. Credit Application Management
- **Create**: Affiliates can submit a new credit application.
- **Read**:
    - Affiliates can see their history.
    - Analysts can filter by status.
- **Evaluate**: Analysts can approve or reject an application based on risk score.

### 3. Risk Integration
- The system must communicate with `Risk Central` to get a credit score.
- If the service is down, the application should handle the error gracefully (or retry).

## Non-Functional Requirements

### 1. Security
- Use BCrypt for password hashing.
- All endpoints (except auth) must be secured via JWT.
- CORS policies should restrict access to trusted frontends.

### 2. Performance
- Response time for application creation should be under 2 seconds.
- Database queries should be optimized with indexes.

### 3. Reliability
- System should log all critical failures.
- Health checks must be available for monitoring (Actuator).

## Use Cases

### UC1: Submit Credit Application
- **Actor**: Affiliate
- **Pre-condition**: User is logged in.
- **Flow**:
    1. User submits application details (amount, term).
    2. System validates input.
    3. System saves application in `PENDING` state.
    4. System returns success response.

### UC2: Evaluate Application (Happy Path)
- **Actor**: Analyst
- **Flow**:
    1. Analyst requests evaluation for application ID X.
    2. System calls Risk Central.
    3. Risk Central returns score > 700.
    4. System updates status to `APPROVED`.
    5. System returns updated application.
