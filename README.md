# Library Management System

A simple CRUD web application built with Java 17, Spring Boot, and MySQL. Provides both REST APIs and a Thymeleaf HTML frontend for managing books and library members.

[![Build Status](https://github.com/AvanAvi/library-management/actions/workflows/maven.yml/badge.svg)](https://github.com/AvanAvi/library-management/actions)

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- Docker (optional, for MySQL)

### Clone and Build
```bash
git clone https://github.com/AvanAvi/library-management.git
cd library-management
mvn clean install
```

### Run Locally (H2 Database)
```bash
mvn spring-boot:run
```
Access at: http://localhost:8081

### Run with Docker (MySQL Database)
```bash
mvn clean package -DskipTests
docker-compose up
```
Access at: http://localhost:8090

## Testing

```bash
# Run unit tests only
mvn test

# Run all tests (unit + integration + e2e)
mvn verify

# Run BDD tests
mvn test -Pbdd-tests

# Generate coverage report
mvn test jacoco:report
# Report: target/site/jacoco/index.html

# Run mutation testing
mvn test org.pitest:pitest-maven:mutationCoverage
# Report: target/pit-reports/index.html
```

## REST API Endpoints

### Books
- `GET /books` - List all books
- `GET /books/{id}` - Get book by ID
- `POST /books` - Create new book
- `PUT /books/{id}` - Update book
- `DELETE /books/{id}` - Delete book

### Members
- `GET /members` - List all members
- `GET /members/{id}` - Get member by ID
- `POST /members` - Create new member
- `PUT /members/{id}` - Update member
- `DELETE /members/{id}` - Delete member

## Web UI

Access the HTML frontend at the root URL:
- `/` - Home page
- `/books-web` - Book management
- `/members-web` - Member management

## Project Structure

```
src/
├── main/
│   ├── java/           # Application code
│   └── resources/      # Templates and config
└── test/
    ├── java/
    │   ├── unit/       # Unit tests (POJO, MockMvc, Mockito)
    │   ├── integration/# Integration tests (TestContainers + MySQL)
    │   ├── e2e/        # E2E tests (Selenium + Thymeleaf)
    │   └── bdd/        # BDD tests (Cucumber)
    └── resources/      # Test config and feature files
```

## Technologies

- **Backend**: Java 17, Spring Boot, Spring Data JPA
- **Frontend**: Thymeleaf, Bootstrap
- **Database**: H2 (dev), MySQL (prod)
- **Testing**: JUnit 5, Mockito, TestContainers, Selenium, Cucumber
- **Build**: Maven
- **CI/CD**: GitHub Actions
