package com.lending.platform.loan_evaluation.service;

import com.lending.platform.loan_evaluation.domain.RiskBand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RiskAssessmentServiceTest {

    private final RiskAssessmentService service = new RiskAssessmentService();

    @Test
    void shouldClassifyLowRisk() {
        assertEquals(RiskBand.LOW, service.classifyRisk(750));
    }

    @Test
    void shouldClassifyMediumRisk() {
        assertEquals(RiskBand.MEDIUM, service.classifyRisk(700));
    }

    @Test
    void shouldClassifyHighRisk() {
        assertEquals(RiskBand.HIGH, service.classifyRisk(620));
    }
}