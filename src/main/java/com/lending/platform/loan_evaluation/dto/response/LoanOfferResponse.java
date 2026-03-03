package com.lending.platform.loan_evaluation.dto.response;

import java.math.BigDecimal;

public class LoanOfferResponse {

    private BigDecimal interestRate;
    private Integer tenureMonths;
    private BigDecimal emi;
    private BigDecimal totalPayable;

    // getters & setters
    
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    public Integer getTenureMonths() {
        return tenureMonths;
    }
    public void setTenureMonths(Integer tenureMonths) {
        this.tenureMonths = tenureMonths;
    }
    public BigDecimal getEmi() {
        return emi;
    }
    public void setEmi(BigDecimal emi) {
        this.emi = emi;
    }
    public BigDecimal getTotalPayable() {
        return totalPayable;
    }
    public void setTotalPayable(BigDecimal totalPayable) {
        this.totalPayable = totalPayable;
    }

}