# Library Management System

Library Management System is a web application built using [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html), Maven, and [Spring Boot](https://start.spring.io/). It provides a RESTful API for managing books and members with a comprehensive HTML frontend for interacting with the API. [Docker](https://www.docker.com/products/docker-desktop/) is utilized through Testcontainers for automated testing in containerized environments.

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

## Getting Started

To run the application, follow the steps below:

1. Clone the repository: \`git clone https://github.com/AvanAvi/library-management.git\`
2. Import the project into Eclipse:
   - Open Eclipse and select **File -> Import**.
   - Choose **Maven -> Existing Maven Projects** and click **Next**.
   - Browse to the directory where you cloned the repository and select the project.
   - Click **Finish** to import the project into Eclipse.
3. In [Eclipse](https://www.eclipse.org/downloads/packages/), right-click on the project and select **Run As -> Maven Install** to build the project and download the required dependencies.
4. Once the Maven build is successful, you can run the application:
   \`\`\`bash
   mvn spring-boot:run
   \`\`\`
5. The application will now be accessible at [http://localhost:8081](http://localhost:8081).

## API Endpoints

The REST API provides the following endpoints:

### Books Management
- **GET localhost:8081/books**: Retrieves a list of all books.
- **GET localhost:8081/books/{id}**: Retrieves a specific book by ID.
- **POST localhost:8081/books**: Creates a new book.
- **PUT localhost:8081/books/{id}**: Updates an existing book.
- **DELETE localhost:8081/books/{id}**: Deletes a book.

### Members Management
- **GET localhost:8081/members**: Retrieves a list of all members.
- **GET localhost:8081/members/{id}**: Retrieves a specific member by ID.
- **POST localhost:8081/members**: Creates a new member.
- **PUT localhost:8081/members/{id}**: Updates an existing member.
- **DELETE localhost:8081/members/{id}**: Deletes a member.

## HTML Frontend

The HTML frontend allows users to interact with the API using a user-friendly interface. It is accessible at [http://localhost:8081](http://localhost:8081) and provides:

- **Book Management**: Add, view, edit, and delete books
- **Member Management**: Add, view, edit, and delete library members
- **Responsive Design**: Bootstrap-powered interface

## Testing

The project includes comprehensive testing:

\`\`\`bash
# Unit tests
mvn test

# Integration tests
mvn failsafe:integration-test failsafe:verify

# All tests with coverage
mvn clean verify

# Mutation testing
mvn org.pitest:pitest-maven:mutationCoverage
\`\`\`

## Project Structure

The project follows a standard Maven structure with comprehensive testing. Key files and directories include:

- **src/main/java**: Contains the Java source code (controllers, services, entities, repositories).
- **src/main/resources**: Contains application properties and Thymeleaf templates.
- **src/test/java**: Contains unit tests, integration tests, end-to-end tests, and UI tests.
  - **unit/**: Unit tests with Mockito
  - **integration/**: Integration tests with Testcontainers
  - **e2e/**: End-to-end tests
  - **ui/**: Selenium-based UI automation tests
- **pom.xml**: Contains Maven configuration and project dependencies.

## Quality Metrics

- **Code Coverage**: 100% (JaCoCo with appropriate exclusions)
- **Mutation Testing**: 100% (PITest)
- **Technical Debt**: 0 (SonarCloud)
- **Code Quality**: A-rating across all metrics
