package com.lending.platform.loan_evaluation.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmiCalculatorTest {

    @Test
    void shouldCalculateCorrectEmi() {

        BigDecimal principal = new BigDecimal("500000");
        BigDecimal annualRate = new BigDecimal("13.5");
        int tenure = 36;

        BigDecimal emi = EmiCalculator.calculateEmi(principal, annualRate, tenure);

        BigDecimal expectedEmi = new BigDecimal("16967.64");

        assertEquals(expectedEmi, emi);
    }

    @Test
    void shouldRoundEmiToTwoDecimalPlaces() {

        BigDecimal principal = new BigDecimal("100000");
        BigDecimal annualRate = new BigDecimal("12");
        int tenure = 12;

        BigDecimal emi = EmiCalculator.calculateEmi(principal, annualRate, tenure);

        assertEquals(2, emi.scale());
    }
}