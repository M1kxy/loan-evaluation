DROP TABLE IF EXISTS loan_decision_audit;

CREATE TABLE loan_decision_audit (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    application_id VARCHAR(100),
    status VARCHAR(20),
    risk_band VARCHAR(10),
    rejection_reasons VARCHAR(1000),
    approved_amount DOUBLE,
    tenure INT,
    decision_time TIMESTAMP
);

DROP TABLE IF EXISTS loan_application;

CREATE TABLE loan_application (
    application_id UUID PRIMARY KEY,
    age INT,
    credit_score INT,
    employment_type VARCHAR(50),
    monthly_income DOUBLE,
    name VARCHAR(100),
    created_at TIMESTAMP,
    emi DOUBLE,
    interest_rate DOUBLE,
    amount DOUBLE,
    purpose VARCHAR(50),
    tenure_months INT,
    rejection_reasons VARCHAR(1000),
    risk_band VARCHAR(50),
    status VARCHAR(20),
    total_payable DOUBLE
);