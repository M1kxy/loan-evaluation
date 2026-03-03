package com.lending.platform.loan_evaluation.service;

import com.lending.platform.loan_evaluation.domain.*;
import com.lending.platform.loan_evaluation.util.EmiCalculator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanEvaluationService {

    private final RiskAssessmentService riskService;
    private final InterestRateService interestRateService;

    public LoanEvaluationService(RiskAssessmentService riskService,
                                 InterestRateService interestRateService) {
        this.riskService = riskService;
        this.interestRateService = interestRateService;
    }

    public LoanApplication evaluate(LoanApplication application) {

        List<String> rejectionReasons = new ArrayList<>();

        Applicant applicant = application.getApplicant();
        Loan loan = application.getLoan();

        // Rule 1: Credit score < 600
        if (applicant.getCreditScore() < 600) {
            rejectionReasons.add("CREDIT_SCORE_BELOW_THRESHOLD");
        }

        // Rule 2: Age + tenure (years) > 65
        int tenureYears = loan.getTenureMonths() / 12;
        if (applicant.getAge() + tenureYears > 65) {
            rejectionReasons.add("AGE_TENURE_LIMIT_EXCEEDED");
        }

        RiskBand riskBand = null;
        BigDecimal interestRate = null;
        BigDecimal emi = null;
        BigDecimal totalPayable = null;

        if (rejectionReasons.isEmpty()) {

            riskBand = riskService.classifyRisk(applicant.getCreditScore());

            interestRate = interestRateService.calculateFinalInterestRate(
                    riskBand,
                    applicant.getEmploymentType(),
                    loan.getAmount()
            );

            emi = EmiCalculator.calculateEmi(
                    loan.getAmount(),
                    interestRate,
                    loan.getTenureMonths()
            );

            BigDecimal sixtyPercentIncome =
                    applicant.getMonthlyIncome().multiply(BigDecimal.valueOf(0.6));

            BigDecimal fiftyPercentIncome =
                    applicant.getMonthlyIncome().multiply(BigDecimal.valueOf(0.5));

            // Rule 3: EMI > 60%
            if (emi.compareTo(sixtyPercentIncome) > 0) {
                rejectionReasons.add("EMI_EXCEEDS_60_PERCENT");
            }

            // Offer rule: EMI > 50%
            if (emi.compareTo(fiftyPercentIncome) > 0) {
                rejectionReasons.add("EMI_EXCEEDS_50_PERCENT");
            }

            totalPayable = emi.multiply(BigDecimal.valueOf(loan.getTenureMonths()));
        }

        application.setRiskBand(riskBand);
        application.setInterestRate(interestRate);
        application.setEmi(emi);
        application.setTotalPayable(totalPayable);
        application.setCreatedAt(LocalDateTime.now());

        if (rejectionReasons.isEmpty()) {
            application.setStatus(ApplicationStatus.APPROVED);
        } else {
            application.setStatus(ApplicationStatus.REJECTED);
            application.setRejectionReasons(String.join(",", rejectionReasons));
        }

        return application;
    }
}