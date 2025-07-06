# Library Management System

Library Management System is a comprehensive web application built using [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html), Maven, and [Spring Boot](https://start.spring.io/). It provides both RESTful APIs and a complete HTML frontend for managing books and members, featuring borrowing/returning functionality, BDD testing with Cucumber, and full Docker containerization support.

| Badge                                                     | URL                                                                                           |
|-----------------------------------------------------------|-----------------------------------------------------------------------------------------------|
| Java CI with Maven and SonarCloud                        | [![Java CI with Maven and SonarCloud](https://github.com/AvanAvi/library-management/workflows/Java%20CI%20with%20Maven%20and%20SonarCloud/badge.svg)](https://github.com/AvanAvi/library-management/actions/workflows/maven.yml) |
| Coverage Status                                           | [![Coverage Status](https://coveralls.io/repos/github/AvanAvi/library-management/badge.svg?branch=main)](https://coveralls.io/github/AvanAvi/library-management?branch=main) |
| Sonarcloud Quality Gate                                  | [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |
| SonarCloud Coverage                                       | [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=coverage)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |
| Technical Debt                                           | [![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |
| Reliability Rating                                       | [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |
| Security Rating                                          | [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |
| Maintainability Rating                                   | [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |

## Features

- **📚 Book Management**: Complete CRUD operations for books with borrowing/returning functionality
- **👥 Member Management**: Full member lifecycle management with borrowing history
- **🌐 HTML Frontend**: Clean, responsive Thymeleaf-based web interface
- **🔌 REST API**: Comprehensive RESTful endpoints for all operations
- **🧪 BDD Testing**: Cucumber-based behavior-driven development tests
- **🐳 Docker Support**: Complete containerization with Docker Compose
- **📊 100% Test Coverage**: Unit, integration, E2E, and UI tests
- **🔍 Mutation Testing**: 100% mutation score with PITest
- **🏗️ CI/CD**: GitHub Actions with SonarCloud integration

## Quick Start

### Option 1: Docker (Recommended)
```bash
git clone https://github.com/AvanAvi/library-management.git
cd library-management
./mvnw clean package -DskipTests
docker-compose up -d
```
Access at: http://localhost:8080

### Option 2: Local Development
```bash
git clone https://github.com/AvanAvi/library-management.git
cd library-management
./mvnw spring-boot:run
```
Access at: http://localhost:8080

## API Endpoints

The REST API provides the following endpoints:

### Books Management
- **GET /books**: Retrieves all books
- **GET /books/{id}**: Retrieves a specific book by ID
- **POST /books**: Creates a new book
- **PUT /books/{id}**: Updates an existing book
- **DELETE /books/{id}**: Deletes a book

### Members Management
- **GET /members**: Retrieves all members
- **GET /members/{id}**: Retrieves a specific member by ID
- **POST /members**: Creates a new member
- **PUT /members/{id}**: Updates an existing member
- **DELETE /members/{id}**: Deletes a member

## Web Interface

The HTML frontend provides a complete library management experience at http://localhost:8080:

### 🏠 Home Dashboard
- Clean overview of library operations
- Quick access to books and members management

### 📚 Book Management
- View all books with availability status
- Add, edit, and delete books
- Borrow books to members
- Return borrowed books
- ISBN validation and duplicate prevention

### 👥 Member Management  
- View all registered members
- Add, edit, and delete members
- View member borrowing history
- Assign books to specific members
- Email validation and unique constraints

### 🔄 Borrowing System
- Real-time availability tracking
- Member-to-book assignment
- Return processing
- Borrowing history per member

## Testing & Quality

### Test Execution
```bash
# Unit tests (61 tests)
./mvnw test

# Integration tests  
./mvnw failsafe:integration-test failsafe:verify

# BDD/Cucumber tests
./mvnw test -P bdd-tests

# All tests with coverage
./mvnw clean verify

# Mutation testing (100% score)
./mvnw org.pitest:pitest-maven:mutationCoverage

# UI tests (with Docker required)
./mvnw failsafe:integration-test -Dtest=*UITest
```

### Test Categories
- **🎯 Unit Tests**: 61 tests covering all controllers, services, and utilities
- **🔗 Integration Tests**: Repository and service layer integration with H2/MySQL
- **🎭 BDD Tests**: Cucumber scenarios for behavior validation
- **🖥️ UI Tests**: Selenium-based browser automation with Testcontainers
- **🧬 Mutation Tests**: PITest with 100% mutation score
- **📊 Coverage**: 100% line and branch coverage

## Docker Support

### Production Deployment
```bash
# Build and start all services
./mvnw clean package -DskipTests
docker-compose up -d

# Services available:
# - Library App: http://localhost:8080
# - MySQL Database: localhost:3306
# - H2 Console: http://localhost:8081
```

### Development Setup
```bash
# Start only databases for local development
docker-compose -f docker-compose.dev.yml up -d
./mvnw spring-boot:run

# Services available:
# - Library App: http://localhost:8080
# - MySQL Dev DB: localhost:3307
# - H2 Console: http://localhost:8082
```

See [DOCKER.md](DOCKER.md) for complete containerization guide.

## Project Structure

```
src/
├── main/java/
│   ├── controller/          # REST and HTML controllers
│   ├── service/            # Business logic layer
│   ├── entity/             # JPA entities (Book, Member)
│   └── repository/         # Data access layer
├── main/resources/
│   ├── templates/          # Thymeleaf HTML templates
│   ├── application*.properties  # Configuration files
│   └── static/             # CSS, JS, images
└── test/java/
    ├── unit/               # Unit tests (MockMvc, Mockito)
    ├── integration/        # Integration tests (Testcontainers)
    ├── bdd/               # Cucumber BDD scenarios
    ├── e2e/               # End-to-end API tests
    └── ui/                # Selenium UI automation tests
```

## Technology Stack

- **Backend**: Java 17, Spring Boot 3.4, Spring Data JPA
- **Frontend**: Thymeleaf, Bootstrap 5, HTML5/CSS3
- **Database**: H2 (dev), MySQL 8.0 (prod)
- **Testing**: JUnit 5, Mockito, Cucumber, Selenium, Testcontainers
- **Build**: Maven, JaCoCo, PITest, SonarCloud
- **DevOps**: Docker, Docker Compose, GitHub Actions

## Quality Metrics

- ✅ **Code Coverage**: 100% (JaCoCo)
- ✅ **Mutation Score**: 100% (PITest)  
- ✅ **Technical Debt**: 0 minutes (SonarCloud)
- ✅ **Code Quality**: A-rating across all metrics
- ✅ **Security**: No vulnerabilities detected
- ✅ **Maintainability**: A-rating
