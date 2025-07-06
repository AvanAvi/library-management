# Docker Setup Guide

This document explains how to run the Library Management System using Docker.

## Prerequisites

- Docker and Docker Compose installed
- Java 17 and Maven (for building the application)

## Quick Start

### 1. Build the Application

```bash
mvn clean package -DskipTests
```

### 2. Run with Docker Compose

```bash
# Full production setup
docker-compose up -d

# Development setup (database only)
docker-compose -f docker-compose.dev.yml up -d
```

## Available Services

### Production Setup (`docker-compose.yml`)

- **Library App**: http://localhost:8080
- **MySQL Database**: localhost:3306
- **H2 Console**: http://localhost:8081

### Development Setup (`docker-compose.dev.yml`)

- **MySQL Database**: localhost:3307
- **H2 Console**: http://localhost:8082

## Environment Configurations

### Docker Profile
The application uses `application-docker.properties` when running in Docker with MySQL.

### Development
Use H2 in-memory database for local development:
```bash
mvn spring-boot:run
```

## Database Credentials

### MySQL
- **Database**: library_db
- **Username**: library_user
- **Password**: library_password
- **Root Password**: rootpassword

### H2 Console
- **URL**: jdbc:h2:mem:testdb
- **Username**: sa
- **Password**: (empty)

## Useful Commands

```bash
# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v

# View logs
docker-compose logs library-app
docker-compose logs mysql-db

# Rebuild and restart
docker-compose up --build -d

# Run BDD tests
mvn test -Dtest=CucumberTestRunner
```

## Troubleshooting

1. **Port conflicts**: Change ports in docker-compose.yml if needed
2. **Database connection issues**: Wait for MySQL to fully start (check logs)
3. **Build issues**: Ensure JAR file exists in target/ directory