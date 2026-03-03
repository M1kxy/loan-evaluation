package com.lending.platform.loan_evaluation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class LoanApplicationRequest {

    @Valid
    @NotNull
    private ApplicantRequest applicant;

    @Valid
    @NotNull
    private LoanRequest loan;

    // getters & setters
    
    public ApplicantRequest getApplicant() {
        return applicant;
    }

    public void setApplicant(ApplicantRequest applicant) {
        this.applicant = applicant;
    }

    public LoanRequest getLoan() {
        return loan;
    }

    public void setLoan(LoanRequest loan) {
        this.loan = loan;
    }

}