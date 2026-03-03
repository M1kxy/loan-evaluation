package com.lending.platform.loan_evaluation.repository;

import com.lending.platform.loan_evaluation.domain.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, UUID> {
}