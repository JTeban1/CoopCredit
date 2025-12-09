package com.riwi.coopcredit.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class CreditApplication {
    private Long id;
    private Long affiliateId;
    private BigDecimal requestedAmount;
    private Integer termMonths;
    private String purpose;
    private ApplicationStatus status;
    private LocalDateTime applicationDate;
    private RiskEvaluation riskEvaluation;

    public enum ApplicationStatus {
        PENDING, APPROVED, REJECTED, IN_EVALUATION
    }

    public CreditApplication() {
    }

    public CreditApplication(Long id, Long affiliateId, BigDecimal requestedAmount, 
                             Integer termMonths, String purpose, ApplicationStatus status,
                             LocalDateTime applicationDate, RiskEvaluation riskEvaluation) {
        this.id = id;
        this.affiliateId = affiliateId;
        this.requestedAmount = requestedAmount;
        this.termMonths = termMonths;
        this.purpose = purpose;
        this.status = status;
        this.applicationDate = applicationDate;
        this.riskEvaluation = riskEvaluation;
    }

    public BigDecimal calculateMonthlyPayment() {
        if (termMonths == null || termMonths == 0) {
            return BigDecimal.ZERO;
        }
        return requestedAmount.divide(
            BigDecimal.valueOf(termMonths),
            2,
            RoundingMode.HALF_UP
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAffiliateId() {
        return affiliateId;
    }

    public void setAffiliateId(Long affiliateId) {
        this.affiliateId = affiliateId;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(BigDecimal requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public RiskEvaluation getRiskEvaluation() {
        return riskEvaluation;
    }

    public void setRiskEvaluation(RiskEvaluation riskEvaluation) {
        this.riskEvaluation = riskEvaluation;
    }
}
