package com.riwi.coopcredit.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreditApplicationResponse(
    Long id,
    Long affiliateId,
    BigDecimal requestedAmount,
    Integer termMonths,
    String purpose,
    String status,
    LocalDateTime applicationDate,
    BigDecimal monthlyPayment,
    RiskEvaluationResponse riskEvaluation
) {}
