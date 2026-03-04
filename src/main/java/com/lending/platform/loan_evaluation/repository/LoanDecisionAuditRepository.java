package com.lending.platform.loan_evaluation.repository;

import com.lending.platform.loan_evaluation.domain.LoanDecisionAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanDecisionAuditRepository 
        extends JpaRepository<LoanDecisionAudit, Long> {
}