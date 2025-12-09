package com.riwi.coopcredit.application.dto.response;

import java.time.LocalDateTime;

public record RiskEvaluationResponse(
    Long id,
    Long creditApplicationId,
    Integer riskScore,
    String riskLevel,
    String evaluationDetails,
    LocalDateTime evaluationDate,
    boolean approved
) {}
