package com.lending.platform.loan_evaluation.dto;

import com.lending.platform.loan_evaluation.domain.LoanPurpose;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class LoanRequest {

    @DecimalMin(value = "10000")
    @DecimalMax(value = "5000000")
    private BigDecimal amount;

    @Min(6)
    @Max(360)
    private Integer tenureMonths;

    @NotNull
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