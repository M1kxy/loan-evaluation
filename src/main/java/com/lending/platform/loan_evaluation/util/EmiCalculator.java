package com.lending.platform.loan_evaluation.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class EmiCalculator {

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    public static BigDecimal calculateEmi(BigDecimal principal,
                                          BigDecimal annualInterestRate,
                                          int tenureMonths) {

        // Convert annual rate to monthly rate
        BigDecimal monthlyRate = annualInterestRate
                .divide(BigDecimal.valueOf(12), 10, ROUNDING_MODE)
                .divide(BigDecimal.valueOf(100), 10, ROUNDING_MODE);

        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);

        BigDecimal power = onePlusR.pow(tenureMonths, new MathContext(20));

        BigDecimal numerator = principal.multiply(monthlyRate).multiply(power);

        BigDecimal denominator = power.subtract(BigDecimal.ONE);

        BigDecimal emi = numerator.divide(denominator, SCALE, ROUNDING_MODE);

        return emi;
    }
}