package com.lending.platform.loan_evaluation.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "loan_decision_audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDecisionAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "application_id")
    private String applicationId;

    private String status;

    @Column(name = "risk_band")
    private String riskBand;

    @Column(name = "rejection_reasons")
    private String rejectionReasons;

    @Column(name = "approved_amount")
    private Double approvedAmount;

    private Integer tenure;

    @Column(name = "decision_time")
    private LocalDateTime decisionTime;
}