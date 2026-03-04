# Development Notes

## Overall Approach

This loan evaluation platform is implemented using a layered Spring Boot
architecture:

Controller → Service → Repository

The service processes loan applications via `POST /applications`,
evaluates eligibility based on defined business rules, calculates EMI
using a standard formula, determines risk classification,
and generates a single offer based on the requested term.

### Flow Overview

1.  Request received in Controller.
2.  Request DTO validated using Bean Validation and invalid requests returns HTTP 400 with meaningful error messages.
3.  LoanEvaluationService orchestrates:
    -   Eligibility checks
    -   Risk band classification
    -   Interest rate calculation
    -   EMI calculation using EmiCalculator
    -   Offer generation logic
4.  Decision (APPROVED / REJECTED) is persisted using
LoanDecisionAuditRepository.
5.  Structured response returned as per assignment specification.

All financial calculations use BigDecimal with scale = 2 and
RoundingMode.HALF_UP to ensure monetary precision.

------------------------------------------------------------------------

## Key Design Decisions

### 1. Domain-Driven Separation

Domain entities (Applicant, Loan, LoanApplication) are separated from
DTOs to maintain a clean boundary between: - API contracts
- Core business logic

This prevents request/response models from leaking into domain logic.

------------------------------------------------------------------------

### 2. Store in Database + Provide schema.sql
for Audit Trail and Compliance

Persisting all loan decisions in a relational database via `schema.sql` ensures:

- Centralized storage prevents data loss and enables ACID transactions.
- SQL enables efficient historical analysis, reporting, and decision traceability.
- Database constraints enforce data validity across the application lifecycle.

Alternative approaches (in-memory, file-based) were rejected due to scalability and durability concerns.

------------------------------------------------------------------------

### 3. Audit Implementation

A dedicated LoanDecisionAudit entity is used to persist all loan
decisions.

This ensures: 
- Traceability
- Auditing
- Historical analysis capability
- Reviewing Descisions

Audit persistence is handled via LoanDecisionAuditRepository.

------------------------------------------------------------------------

### 4. Risk Band Model

Applicants are categorized into risk tiers: 
- LOW
- MEDIUM
- HIGH

Risk band impacts interest rate calculation through risk premium
addition.

Risk band is included only in APPROVED responses, and null for REJECTED
responses, as required.

------------------------------------------------------------------------

### 5. Addition of Unit Test Cases

Unit test cases have been added to validate core business logic including:
- Eligibility rule validation
- Risk band classification correctness
- Interest rate calculation accuracy
- EMI computation with various tenure and amount combinations
- Audit persistence verification

------------------------------------------------------------------------

## Trade-offs Considered

### Stateless Service Layer

The services are stateless to ensure scalability and thread safety. No
in-memory state is maintained between requests.

Trade-off: Slightly more database interaction, but improved horizontal
scalability.

------------------------------------------------------------------------

### Database-Centric Audit

All decisions are persisted immediately instead of caching.

Trade-off: Higher write operations, but ensures strong data integrity
and compliance traceability.

------------------------------------------------------------------------

### Synchronous Processing

Loan evaluation is handled synchronously within a single
request-response cycle.

Trade-off: Simpler implementation and easier debugging. Asynchronous
processing could improve scalability but would increase architectural
complexity.

------------------------------------------------------------------------

### Centralized Rule Evaluation

Eligibility rules are implemented using conditional logic inside the
service layer.

Trade-off: Simple and readable for assignment scope. In a large-scale
system, a rule engine or policy framework would improve extensibility.

------------------------------------------------------------------------

## Assumptions Made

-   Interest rates are calculated dynamically via InterestRateService.
-   Loan purposes, EmploymentType, ApplicationStatus are predefined in the LoanPurpose enum.
-   EMI calculation follows the standard amortization formula.
-   Only one offer is generated using the requested tenure.
-   Tenure-based age validation uses year conversion derived from tenure in months.
-   Calculation of AGE_TENURE_LIMIT_EXCEEDED validates that applicant age plus loan tenure (in years) does not exceed 65, using BigDecimal for precise fractional year conversion from tenure months.

------------------------------------------------------------------------

## Improvements with More Time

### 1. Configuration Externalization

Move configurable values such as: - Base interest rate
- Risk premiums
- Income thresholds (50%, 60%)

to application.yml for easy access and if change required.

------------------------------------------------------------------------

### 2. Caching Layer

Introduce cache: - Risk band thresholds
- Interest rate configurations

This would reduce computation time in scenarios where data is high volume.

------------------------------------------------------------------------

### 3. Async Processing

Introduce message queues (kafka) to: - Process decisions
asynchronously
- Publish loan decision events
- Improve scalability

------------------------------------------------------------------------

### 4. Integration with Broader Banking System

Integrate this loan evaluation service into a banking platform:

- Loan decisions are persisted in the core banking system database
- Real-time synchronization with customer profiles and account history

------------------------------------------------------------------------


### 6. Observability & Monitoring

Add: - Micrometer metrics
- Prometheus integration
- Structured logging
- Request correlation IDs

------------------------------------------------------------------------

### 7. Expanded Test Coverage

Increase coverage with: - Boundary condition tests
- Full integration tests
- Repository-level tests
- Performance benchmarking tests

------------------------------------------------------------------------

## Refactoring & Development Evolution

The project was developed incrementally with logical separation of
commits: - Initial project setup
- Domain modeling
- Validation implementation
- EMI calculation utility
- Risk and interest logic
- Offer generation
- Response formatting corrections
- Audit persistence
- Unit Test Cases
- Refactoring and cleanup

