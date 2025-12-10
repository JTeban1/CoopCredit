# Setup Guide

## Prerequisites
Ensure you have the following installed on your machine:
- **Java 17** or higher
- **Maven** (or use the provided `mvnw` wrapper)
- **Docker** and **Docker Compose**
- **Git**

## Environment Variables
The application relies on several environment variables. You can set them in your IDE or system environment.

| Variable | Description | Default Value |
|----------|-------------|---------------|
| `PORT` | The port the application runs on | `8080` |
| `JWT_SECRET` | Secret key for signing JWT tokens | `mySecretKeyForJWTToken...` |
| `JWT_EXPIRATION_MINUTES` | Token validity duration | `1440` (24 hours) |
| `RISK_CENTRAL_URL` | URL of the Risk Central Service | `http://localhost:8081` |
| `DB_URL` | Application Database URL | `jdbc:postgresql://localhost:5432/coopcredit` |
| `DB_USERNAME` | Database Username | `postgres` |
| `DB_PASSWORD` | Database Password | `password` |

## Installation & Running

1. **Clone the repository**:
   ```bash
   git clone https://github.com/riwi/coopcredit.git
   cd coopcredit/credit-application-service
   ```

2. **Start the Database**:
   Use Docker Compose to start the PostgreSQL database and other dependencies.
   ```bash
   docker-compose up -d
   ```

3. **Build the Application**:
   ```bash
   ./mvnw clean install
   ```

4. **Run the Application**:
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Verify it's running**:
   - Health Check: `http://localhost:8080/actuator/health`
   - API Base: `http://localhost:8080/api/applications`

## Troubleshooting
- **Database Connection Refused**: Ensure the Docker container for Postgres is running and port 5432 is exposed.
- **JWT Errors**: Check if the `JWT_SECRET` matches between services if distributed.
