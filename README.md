# Library Management System

Library Management System is a web application built using Java 17, Maven, and Spring Boot. It provides RESTful APIs and HTML frontend for managing books and members with borrowing/returning functionality.

# Library Management System

# Library Management System

| Category | Badge |
|----------|-------|
| **Build** | [![Build Status](https://github.com/AvanAvi/library-management/actions/workflows/maven.yml/badge.svg)](https://github.com/AvanAvi/library-management/actions) |
| **Coverage** | [![Coverage Status](https://coveralls.io/repos/github/AvanAvi/library-management/badge.svg)](https://coveralls.io/github/AvanAvi/library-management) |
| **Quality Gate** | [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |
| **Coverage** | [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=coverage)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |
| **Technical Debt** | [![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |
| **Bugs** | [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=bugs)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |
| **Vulnerabilities** | [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |
| **Code Smells** | [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |
| **Maintainability** | [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |
| **Reliability** | [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |
| **Security** | [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |

## Quick Start

### Local Development
```bash
./mvnw spring-boot:run
```
Access at: http://localhost:8081

### Docker
```bash
./mvnw clean package -DskipTests
docker-compose up
```
Access at: http://localhost:8080

## Testing

```bash
./mvnw clean verify
```
