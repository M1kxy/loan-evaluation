package com.lending.platform.loan_evaluation.dto.response;

import com.lending.platform.loan_evaluation.domain.ApplicationStatus;
import com.lending.platform.loan_evaluation.domain.RiskBand;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.UUID;


public class LoanApplicationResponse {

    private UUID applicationId;
    private ApplicationStatus status;
    private RiskBand riskBand;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LoanOfferResponse offer;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> rejectionReasons;

    // getters & setters

    public UUID getApplicationId() {
        return applicationId;
    }
    public void setApplicationId(UUID applicationId) {
        this.applicationId = applicationId;
    }
    public ApplicationStatus getStatus() {
        return status;
    }
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
    public RiskBand getRiskBand() {
        return riskBand;
    }
    public void setRiskBand(RiskBand riskBand) {
        this.riskBand = riskBand;
    }
    public LoanOfferResponse getOffer() {
        return offer;
    }
    public void setOffer(LoanOfferResponse offer) {
        this.offer = offer;
    }
    public List<String> getRejectionReasons() {
        return rejectionReasons;
    }
    public void setRejectionReasons(List<String> rejectionReasons) {
        this.rejectionReasons = rejectionReasons;
    }


}