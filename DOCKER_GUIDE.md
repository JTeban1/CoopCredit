# CoopCredit - Docker Deployment Guide

## Services Overview

The docker-compose setup includes the following services:

| Service | Port | Description |
|---------|------|-------------|
| **frontend** | 80 | React frontend (Nginx) |
| **credit-application-service** | 8080 | Main Spring Boot API |
| **risk-central-mock-service** | 8081 | Mock risk evaluation service |
| **db** | 5432 | PostgreSQL database |
| **prometheus** | 9090 | Metrics collection |
| **grafana** | 3000 | Metrics visualization |

## Quick Start

### Build and Start All Services

```bash
# Build and start all services
docker-compose up --build

# Or run in detached mode
docker-compose up -d --build
```

### Access the Application

- **Frontend**: http://localhost
- **Backend API**: http://localhost:8080
- **Risk Service**: http://localhost:8081
- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3000 (admin/admin)

### View Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f frontend
docker-compose logs -f credit-application-service
```

### Stop Services

```bash
# Stop services
docker-compose down

# Stop and remove volumes (clean slate)
docker-compose down -v
```

## Individual Service Commands

### Start Only Specific Services

```bash
# Start only database
docker-compose up db

# Start backend services (db + risk + api)
docker-compose up db risk-central-mock-service credit-application-service

# Start everything except monitoring
docker-compose up db risk-central-mock-service credit-application-service frontend
```

### Rebuild Specific Service

```bash
# Rebuild frontend only
docker-compose build frontend
docker-compose up -d frontend

# Rebuild backend only
docker-compose build credit-application-service
docker-compose up -d credit-application-service
```

## Health Checks

All services have health checks configured:

```bash
# Check service health
docker-compose ps

# Inspect specific service
docker inspect coopcredit-frontend
```

## Resource Limits

Each service has CPU and memory limits:
- **Frontend**: 0.25 CPU, 128MB RAM
- **Backend Services**: 0.50 CPU, 512MB RAM
- **Database**: 0.50 CPU, 512MB RAM
- **Monitoring**: 0.50 CPU, 256-512MB RAM

## Troubleshooting

### Frontend Not Loading

```bash
# Check frontend logs
docker-compose logs frontend

# Restart frontend
docker-compose restart frontend
```

### Backend Connection Issues

```bash
# Check if backend is healthy
docker-compose ps credit-application-service

# Check backend logs
docker-compose logs credit-application-service

# Verify database connection
docker-compose exec db psql -U postgres -d coopcredit -c "SELECT 1;"
```

### Database Issues

```bash
# Access database
docker-compose exec db psql -U postgres -d coopcredit

# Reset database
docker-compose down -v
docker-compose up db
```

## Development vs Production

### Development (Current Setup)
- Frontend served by Nginx
- Backend connects to containerized database
- All services in same network

### Production Considerations
1. Use environment-specific `.env` files
2. Enable HTTPS with SSL certificates
3. Use production-grade database (not containerized)
4. Implement proper secrets management
5. Add reverse proxy (Nginx/Traefik)
6. Enable CORS properly
7. Set up proper logging and monitoring

## Network Configuration

All services are on the `coopcredit-network` bridge network, allowing:
- Frontend → Backend: `http://credit-application-service:8080`
- Backend → Risk Service: `http://risk-central-mock-service:8081`
- Backend → Database: `jdbc:postgresql://db:5432/coopcredit`

## Volume Management

```bash
# List volumes
docker volume ls | grep coopcredit

# Inspect postgres volume
docker volume inspect coopcredit_postgres_data

# Backup database
docker-compose exec db pg_dump -U postgres coopcredit > backup.sql

# Restore database
docker-compose exec -T db psql -U postgres coopcredit < backup.sql
```

## Useful Commands

```bash
# Remove all stopped containers
docker-compose rm

# Rebuild without cache
docker-compose build --no-cache

# Scale a service (if stateless)
docker-compose up -d --scale frontend=2

# Execute command in container
docker-compose exec frontend sh
docker-compose exec credit-application-service sh

# View resource usage
docker stats
```
