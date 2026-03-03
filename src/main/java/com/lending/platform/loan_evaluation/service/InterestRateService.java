package com.lending.platform.loan_evaluation.service;

import com.lending.platform.loan_evaluation.domain.EmploymentType;
import com.lending.platform.loan_evaluation.domain.RiskBand;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class InterestRateService {

    private static final BigDecimal BASE_RATE = BigDecimal.valueOf(12);

    public BigDecimal calculateFinalInterestRate(RiskBand riskBand,
                                                 EmploymentType employmentType,
                                                 BigDecimal loanAmount) {

        BigDecimal finalRate = BASE_RATE;

        // Risk premium
        switch (riskBand) {
            case MEDIUM -> finalRate = finalRate.add(BigDecimal.valueOf(1.5));
            case HIGH -> finalRate = finalRate.add(BigDecimal.valueOf(3));
            case LOW -> { /* no addition */ }
        }

        // Employment premium
        if (employmentType == EmploymentType.SELF_EMPLOYED) {
            finalRate = finalRate.add(BigDecimal.valueOf(1));
        }

        // Loan size premium
        if (loanAmount.compareTo(BigDecimal.valueOf(1_000_000)) > 0) {
            finalRate = finalRate.add(BigDecimal.valueOf(0.5));
        }

        return finalRate.setScale(2, RoundingMode.HALF_UP);
    }
}