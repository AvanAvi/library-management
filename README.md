# Library Management System

Library Management System is a web application built using Java 17, Maven, and Spring Boot. It provides RESTful APIs and HTML frontend for managing books and members with borrowing/returning functionality.

| Badge                                                     | URL                                                                                           |
|-----------------------------------------------------------|-----------------------------------------------------------------------------------------------|
| Coveralls                                                 | [![Coverage Status](https://coveralls.io/repos/github/AvanAvi/library-management/badge.svg?branch=main)](https://coveralls.io/github/AvanAvi/library-management?branch=main) |
| SonarCloud Coverage                                       | [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=coverage)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |

## Quick Start

### Local Development
```bash
git clone https://github.com/AvanAvi/library-management.git
cd library-management
./mvnw spring-boot:run
```
Access at: http://localhost:8081

### Docker
```bash
git clone https://github.com/AvanAvi/library-management.git
cd library-management
./mvnw clean package -DskipTests
docker-compose up -d
```
Access at: http://localhost:8080

## Testing

```bash
# Run all tests
./mvnw clean verify

# Unit tests only
./mvnw test
```
