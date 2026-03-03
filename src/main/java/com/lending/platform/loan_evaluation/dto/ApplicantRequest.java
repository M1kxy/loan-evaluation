package com.lending.platform.loan_evaluation.dto;

import com.lending.platform.loan_evaluation.domain.EmploymentType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ApplicantRequest {

    @NotBlank
    private String name;

    @Min(21)
    @Max(60)
    private Integer age;

    @DecimalMin(value = "0.01", inclusive = true)
    private BigDecimal monthlyIncome;

    @NotNull
    private EmploymentType employmentType;

    @Min(300)
    @Max(900)
    private Integer creditScore;
    
    // getters & setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public Integer getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(Integer creditScore) {
        this.creditScore = creditScore;
    }

}