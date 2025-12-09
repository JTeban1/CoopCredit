package com.riwi.coopcredit.domain.model;

import java.time.LocalDateTime;

public class RiskEvaluation {
    private Long id;
    private Long creditApplicationId;
    private Integer riskScore;
    private String riskLevel;
    private String evaluationDetails;
    private LocalDateTime evaluationDate;

    public RiskEvaluation() {
    }

    public RiskEvaluation(Long id, Long creditApplicationId, Integer riskScore, 
                          String riskLevel, String evaluationDetails, 
                          LocalDateTime evaluationDate) {
        this.id = id;
        this.creditApplicationId = creditApplicationId;
        this.riskScore = riskScore;
        this.riskLevel = riskLevel;
        this.evaluationDetails = evaluationDetails;
        this.evaluationDate = evaluationDate;
    }

    public boolean isApproved() {
        return riskScore != null && riskScore >= 600;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreditApplicationId() {
        return creditApplicationId;
    }

    public void setCreditApplicationId(Long creditApplicationId) {
        this.creditApplicationId = creditApplicationId;
    }

    public Integer getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(Integer riskScore) {
        this.riskScore = riskScore;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getEvaluationDetails() {
        return evaluationDetails;
    }

    public void setEvaluationDetails(String evaluationDetails) {
        this.evaluationDetails = evaluationDetails;
    }

    public LocalDateTime getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(LocalDateTime evaluationDate) {
        this.evaluationDate = evaluationDate;
    }
}
