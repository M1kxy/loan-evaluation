# Loan-Application

A Spring Boot REST service that evaluates loan applications and determines whether a single loan offer (based on requested tenure) can be approved.
## Getting Started
The code is present in develop branch

### Prerequisites
- Java 11 or higher
- Maven 3.6+

### Setting up the Project
- Run 
```bash
git clone https://github.com/M1kxy/loan-evaluation.git
cd loan-evaluation
git checkout develop
```

### Running the Application
```bash
mvn clean install
mvn spring-boot:run
```
The application starts on `http://localhost:8080`

### Running Tests
```bash
mvn test
```
Test classes are located in `src/test/java/com/lending/platform/loan_evaluation/`

### Database
- H2 Database Console Access Point: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:file:./data/loan-db
- Driver Class: org.h2.Driver
- username: sa
- **Description** Provides access to the H2 in-memory database console for development and testing purposes
- **Location**: `src/main/resources/schema.sql`
- **Configuration**: `src/main/resources/application.yaml`
- The database schema is automatically initialized on application startup
- Update `application.yaml` with your database server details (host, port, credentials)