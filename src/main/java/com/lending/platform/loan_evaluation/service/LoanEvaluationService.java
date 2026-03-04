package com.lending.platform.loan_evaluation.service;

import com.lending.platform.loan_evaluation.domain.*;
import com.lending.platform.loan_evaluation.repository.LoanApplicationRepository;
import com.lending.platform.loan_evaluation.repository.LoanDecisionAuditRepository;
import com.lending.platform.loan_evaluation.util.EmiCalculator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanEvaluationService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final RiskAssessmentService riskService;
    private final InterestRateService interestRateService;
    private final LoanDecisionAuditRepository loanDecisionAuditRepository;

    public LoanEvaluationService(RiskAssessmentService riskService,
                                 InterestRateService interestRateService,
                                 LoanDecisionAuditRepository loanDecisionAuditRepository,
                                 LoanApplicationRepository loanApplicationRepository) {
        this.riskService = riskService;
        this.interestRateService = interestRateService;
        this.loanDecisionAuditRepository = loanDecisionAuditRepository;
        this.loanApplicationRepository = loanApplicationRepository;
    }

    public LoanApplication evaluate(LoanApplication application) {

        List<String> rejectionReasons = new ArrayList<>();

        Applicant applicant = application.getApplicant();
        Loan loan = application.getLoan();

        if (applicant.getCreditScore() < 600) {
            rejectionReasons.add("CREDIT_SCORE_BELOW_THRESHOLD");
        }

        BigDecimal tenureYears = BigDecimal.valueOf(loan.getTenureMonths())
                .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);

        BigDecimal agePlusTenure = BigDecimal.valueOf(applicant.getAge())
                .add(tenureYears);

        if (agePlusTenure.compareTo(BigDecimal.valueOf(65)) > 0) {
            rejectionReasons.add("AGE_TENURE_LIMIT_EXCEEDED");
        }

        RiskBand riskBand = riskService.classifyRisk(applicant.getCreditScore());

        BigDecimal interestRate = interestRateService.calculateFinalInterestRate(
                riskBand,
                applicant.getEmploymentType(),
                loan.getAmount());

        BigDecimal emi = EmiCalculator.calculateEmi(
                loan.getAmount(),
                interestRate,
                loan.getTenureMonths());

        BigDecimal sixtyPercentIncome = applicant.getMonthlyIncome().multiply(BigDecimal.valueOf(0.6));
        BigDecimal fiftyPercentIncome = applicant.getMonthlyIncome().multiply(BigDecimal.valueOf(0.5));

        if (emi.compareTo(sixtyPercentIncome) > 0) {
            rejectionReasons.add("EMI_EXCEEDS_60_PERCENT");
        } else if (emi.compareTo(fiftyPercentIncome) > 0) {
            rejectionReasons.add("EMI_EXCEEDS_50_PERCENT");
        }

        BigDecimal totalPayable = emi.multiply(BigDecimal.valueOf(loan.getTenureMonths()));

        application.setRiskBand(riskBand);
        application.setInterestRate(interestRate);
        application.setEmi(emi);
        application.setTotalPayable(totalPayable);
        application.setCreatedAt(LocalDateTime.now());

        if (rejectionReasons.isEmpty()) {
            application.setStatus(ApplicationStatus.APPROVED);
        } else {
            application.setStatus(ApplicationStatus.REJECTED);
            application.setRiskBand(null);
            application.setRejectionReasons(String.join(",", rejectionReasons));
        }

       
        LoanApplication savedApplication = loanApplicationRepository.save(application);

        
        LoanDecisionAudit audit = LoanDecisionAudit.builder()
                .applicationId(savedApplication.getApplicationId().toString())
                .status(savedApplication.getStatus().name())
                .riskBand(savedApplication.getRiskBand() != null
                        ? savedApplication.getRiskBand().name()
                        : null)
                .approvedAmount(loan.getAmount() != null
                        ? loan.getAmount().doubleValue()
                        : null)
                .tenure(loan.getTenureMonths())
                .decisionTime(LocalDateTime.now())
                .rejectionReasons(savedApplication.getRejectionReasons())
                .build();

        loanDecisionAuditRepository.save(audit);

        return savedApplication;
    }
}