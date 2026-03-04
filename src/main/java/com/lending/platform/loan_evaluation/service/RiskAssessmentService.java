package com.lending.platform.loan_evaluation.service;

import com.lending.platform.loan_evaluation.domain.RiskBand;
import org.springframework.stereotype.Service;

@Service
public class RiskAssessmentService {

    public RiskBand classifyRisk(int creditScore) {

        if (creditScore >= 750) {
            return RiskBand.LOW;
        } else if (creditScore >= 650) {
            return RiskBand.MEDIUM;
        } else if (creditScore >= 600) {
            return RiskBand.HIGH;
        } else {
            return RiskBand.HIGH; // reject application if credit score is below 600
        }
    }
}