package com.lending.platform.loan_evaluation.controller;

import com.lending.platform.loan_evaluation.domain.*;
import com.lending.platform.loan_evaluation.dto.LoanApplicationRequest;
import com.lending.platform.loan_evaluation.dto.response.LoanApplicationResponse;
import com.lending.platform.loan_evaluation.dto.response.LoanOfferResponse;
import com.lending.platform.loan_evaluation.repository.LoanApplicationRepository;
import com.lending.platform.loan_evaluation.service.LoanEvaluationService;
import jakarta.validation.Valid;

import java.util.List;

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
    public LoanApplicationResponse createApplication(
            @Valid @RequestBody LoanApplicationRequest request) {

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

        LoanApplication evaluated = evaluationService.evaluate(application);
        LoanApplication saved = repository.save(evaluated);

        // Map to response
        LoanApplicationResponse response = new LoanApplicationResponse();
        response.setApplicationId(saved.getApplicationId());
        response.setStatus(saved.getStatus());
        response.setRiskBand(saved.getRiskBand());

        if (saved.getStatus() == ApplicationStatus.APPROVED) {
            LoanOfferResponse offer = new LoanOfferResponse();
            offer.setInterestRate(saved.getInterestRate());
            offer.setTenureMonths(saved.getLoan().getTenureMonths());
            offer.setEmi(saved.getEmi());
            offer.setTotalPayable(saved.getTotalPayable());
            response.setOffer(offer);
        } else {
            response.setRejectionReasons(
                    List.of(saved.getRejectionReasons().split(",")));
        }

        return response;
    }
}