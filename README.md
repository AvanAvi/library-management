<h1>Library Management System</h1>

<p>Library Management System is a web application built using <a href="https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html" target="_blank">Java 17</a>, Maven, and <a href="https://spring.io/projects/spring-boot" target="_blank">Spring Boot</a>. It provides RESTful APIs and HTML frontend for managing books and members with borrowing/returning functionality. <a href="https://www.docker.com/products/docker-desktop/" target="_blank">Docker</a> is utilized to containerize the application, enabling easy deployment in any environment.</p>

| Badge | URL |
|-------|-----|
| Build Status | [![Build Status](https://github.com/AvanAvi/library-management/actions/workflows/maven.yml/badge.svg)](https://github.com/AvanAvi/library-management/actions) |
| Coverage Status | [![Coverage Status](https://coveralls.io/repos/github/AvanAvi/library-management/badge.svg)](https://coveralls.io/github/AvanAvi/library-management) |
| Quality Gate Status | [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |
| Coverage | [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=coverage)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |
| Duplicated Lines (%) | [![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |
| Technical Debt | [![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |
| Bugs | [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=bugs)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |
| Vulnerabilities | [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |
| Code Smells | [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |
| Maintainability Rating | [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |
| Reliability Rating | [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |
| Security Rating | [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=AvanAvi_library-management&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=AvanAvi_library-management) |

<h2>Getting Started</h2>

<p>To run the application, follow the steps below:</p>

<ol>
  <li>Clone the repository: <code>git clone https://github.com/AvanAvi/library-management.git</code></li>
  <li>Import the project into Eclipse:
    <ul>
      <li>Open Eclipse and select <strong>File -> Import</strong>.</li>
      <li>Choose <strong>Maven -> Existing Maven Projects</strong> and click <strong>Next</strong>.</li>
      <li>Browse to the directory where you cloned the repository and select the project.</li>
      <li>Click <strong>Finish</strong> to import the project into Eclipse.</li>
    </ul>
  </li>
  <li>In <a href="https://www.eclipse.org/downloads/packages/" target="_blank">Eclipse</a>, right-click on the project and select <strong>Run As -> Maven Install</strong> to build the project and download the required dependencies.</li>
  <li>Once the Maven build is successful, you can proceed with running the application.</li>
</ol>

<h3>Local Development</h3>
<p>Run the application locally using H2 in-memory database:</p>
<pre><code>./mvnw spring-boot:run</code></pre>
<p>Access at: <a href="http://localhost:8081" target="_blank">http://localhost:8081</a></p>

<h3>Docker</h3>
<p>Run the application using Docker with MySQL database:</p>
<pre><code>./mvnw clean package -DskipTests
# Start MySQL first
docker-compose up -d mysql-db
sleep 30
# Then start the application
docker-compose up library-app</code></pre>
<p>Access at: <a href="http://localhost:8090" target="_blank">http://localhost:8090</a></p>

<h2>API Endpoints</h2>

<p>The REST API provides the following endpoints:</p>

<h3>Book Management</h3>
<ul>
  <li><strong>GET /api/books</strong>: Retrieves a list of all books.</li>
  <li><strong>GET /api/books/{id}</strong>: Retrieves a specific book by ID.</li>
  <li><strong>POST /api/books</strong>: Creates a new book.</li>
  <li><strong>PUT /api/books/{id}</strong>: Updates an existing book.</li>
  <li><strong>DELETE /api/books/{id}</strong>: Deletes a book.</li>
</ul>

<h3>Member Management</h3>
<ul>
  <li><strong>GET /api/members</strong>: Retrieves a list of all members.</li>
  <li><strong>GET /api/members/{id}</strong>: Retrieves a specific member by ID.</li>
  <li><strong>POST /api/members</strong>: Creates a new member.</li>
  <li><strong>PUT /api/members/{id}</strong>: Updates an existing member.</li>
  <li><strong>DELETE /api/members/{id}</strong>: Deletes a member.</li>
</ul>

<h3>Borrowing Operations</h3>
<ul>
  <li><strong>POST /api/books/{bookId}/borrow/{memberId}</strong>: Borrow a book to a member.</li>
  <li><strong>POST /api/books/{bookId}/return</strong>: Return a borrowed book.</li>
</ul>

<h2>HTML Frontend</h2>

<p>The HTML frontend allows users to interact with the API using a user-friendly interface. It provides pages for:</p>
<ul>
  <li>Book management (add, edit, delete, view books)</li>
  <li>Member management (add, edit, delete, view members)</li>
  <li>Borrowing operations (borrow and return books)</li>
</ul>

<h2>Testing</h2>

<p>Run all tests including unit, integration, and end-to-end tests:</p>
<pre><code>./mvnw clean verify</code></pre>

<h2>Project Structure</h2>

<p>The project follows a standard Maven structure with comprehensive testing:</p>

<ul>
    <li><strong>src/main/java</strong>: Contains the Java source code including controllers, services, repositories, entities, and DTOs.</li>
    <li><strong>src/main/resources</strong>: Contains application properties, templates, and static files.</li>
    <li><strong>src/test/java</strong>: Contains comprehensive test suite:
      <ul>
        <li><strong>unit/</strong>: Unit tests for all components</li>
        <li><strong>integration/</strong>: Integration tests for controllers and repositories</li>
        <li><strong>e2e/</strong>: End-to-end tests for complete workflows</li>
        <li><strong>ui/</strong>: UI tests using Selenium WebDriver</li>
        <li><strong>bdd/</strong>: Behavior-driven development tests using Cucumber</li>
      </ul>
    </li>
    <li><strong>src/test/resources</strong>: Contains test properties and BDD feature files.</li>
    <li><strong>pom.xml</strong>: Contains Maven configuration with comprehensive testing and quality assurance setup.</li>
</ul>