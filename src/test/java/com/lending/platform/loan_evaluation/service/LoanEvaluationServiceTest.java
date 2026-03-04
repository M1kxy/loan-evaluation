package com.lending.platform.loan_evaluation.service;

import com.lending.platform.loan_evaluation.domain.*;
import com.lending.platform.loan_evaluation.repository.LoanApplicationRepository;
import com.lending.platform.loan_evaluation.repository.LoanDecisionAuditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoanEvaluationServiceTest {

    private LoanApplicationRepository loanApplicationRepository;
    private LoanDecisionAuditRepository loanDecisionAuditRepository;
    private RiskAssessmentService riskAssessmentService;
    private InterestRateService interestRateService;

    private LoanEvaluationService service;

    @BeforeEach
    void setup() {
        loanApplicationRepository = mock(LoanApplicationRepository.class);
        loanDecisionAuditRepository = mock(LoanDecisionAuditRepository.class);
        riskAssessmentService = new RiskAssessmentService();
        interestRateService = mock(InterestRateService.class);

        service = new LoanEvaluationService(
                riskAssessmentService,
                interestRateService,
                loanDecisionAuditRepository,
                loanApplicationRepository
        );

        // Mock repository save behavior
        when(loanApplicationRepository.save(any()))
                .thenAnswer(invocation -> {
                    LoanApplication app = invocation.getArgument(0);
                    app.setApplicationId(UUID.randomUUID());
                    return app;
                });
    }

    @Test
    void shouldRejectWhenCreditScoreBelow600() {

        LoanApplication application = buildApplication(550);

        when(interestRateService.calculateFinalInterestRate(any(), any(), any()))
                .thenReturn(new BigDecimal("12"));

        LoanApplication result = service.evaluate(application);

        assertEquals(ApplicationStatus.REJECTED, result.getStatus());
        assertTrue(result.getRejectionReasons()
                .contains("CREDIT_SCORE_BELOW_THRESHOLD"));

        verify(loanDecisionAuditRepository, times(1)).save(any());
    }

    @Test
    void shouldApproveValidApplication() {

        LoanApplication application = buildApplication(720);

        when(interestRateService.calculateFinalInterestRate(any(), any(), any()))
                .thenReturn(new BigDecimal("12"));

        LoanApplication result = service.evaluate(application);

        assertEquals(ApplicationStatus.APPROVED, result.getStatus());
        assertNotNull(result.getRiskBand());
        assertNotNull(result.getEmi());
        assertNotNull(result.getTotalPayable());
    }

    private LoanApplication buildApplication(int creditScore) {

        Applicant applicant = new Applicant();
        applicant.setAge(30);
        applicant.setCreditScore(creditScore);
        applicant.setMonthlyIncome(new BigDecimal("75000"));
        applicant.setEmploymentType(EmploymentType.SALARIED);

        Loan loan = new Loan();
        loan.setAmount(new BigDecimal("500000"));
        loan.setTenureMonths(36);
        loan.setPurpose(LoanPurpose.PERSONAL);

        LoanApplication application = new LoanApplication();
        application.setApplicant(applicant);
        application.setLoan(loan);

        return application;
    }
}