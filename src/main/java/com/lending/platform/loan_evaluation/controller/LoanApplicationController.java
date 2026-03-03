package com.lending.platform.loan_evaluation.controller;

import com.lending.platform.loan_evaluation.domain.*;
import com.lending.platform.loan_evaluation.dto.LoanApplicationRequest;
import com.lending.platform.loan_evaluation.repository.LoanApplicationRepository;
import com.lending.platform.loan_evaluation.service.LoanEvaluationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/applications")
public class LoanApplicationController {

    private final LoanEvaluationService evaluationService;
    private final LoanApplicationRepository repository;

    public LoanApplicationController(LoanEvaluationService evaluationService,
                                     LoanApplicationRepository repository) {
        this.evaluationService = evaluationService;
        this.repository = repository;
    }

    @PostMapping
    public LoanApplication createApplication(
            @Valid @RequestBody LoanApplicationRequest request) {

        // Map DTO → Entity
        LoanApplication application = new LoanApplication();

        Applicant applicant = new Applicant();
        applicant.setName(request.getApplicant().getName());
        applicant.setAge(request.getApplicant().getAge());
        applicant.setMonthlyIncome(request.getApplicant().getMonthlyIncome());
        applicant.setEmploymentType(request.getApplicant().getEmploymentType());
        applicant.setCreditScore(request.getApplicant().getCreditScore());

        Loan loan = new Loan();
        loan.setAmount(request.getLoan().getAmount());
        loan.setTenureMonths(request.getLoan().getTenureMonths());
        loan.setPurpose(request.getLoan().getPurpose());

        application.setApplicant(applicant);
        application.setLoan(loan);

        // Evaluate
        LoanApplication evaluated = evaluationService.evaluate(application);

        // Save for audit
        return repository.save(evaluated);
    }
}