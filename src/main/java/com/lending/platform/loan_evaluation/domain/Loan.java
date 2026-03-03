package com.lending.platform.loan_evaluation.domain;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Loan {

    private BigDecimal amount;

    private Integer tenureMonths;

    private LoanPurpose purpose;

    // getters & setters

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getTenureMonths() {
        return tenureMonths;
    }

    public void setTenureMonths(Integer tenureMonths) {
        this.tenureMonths = tenureMonths;
    }

    public LoanPurpose getPurpose() {
        return purpose;
    }

    public void setPurpose(LoanPurpose purpose) {
        this.purpose = purpose;
    }
}