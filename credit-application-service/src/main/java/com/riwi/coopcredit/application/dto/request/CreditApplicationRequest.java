package com.riwi.coopcredit.application.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreditApplicationRequest(
    @NotNull(message = "Affiliate ID is required")
    Long affiliateId,

    @NotNull(message = "Requested amount is required")
    @Positive(message = "Requested amount must be greater than zero")
    BigDecimal requestedAmount,

    @NotNull(message = "Term months is required")
    @Min(value = 1, message = "Term must be at least 1 month")
    @Max(value = 60, message = "Term cannot exceed 60 months")
    Integer termMonths,

    @Size(max = 500, message = "Purpose must not exceed 500 characters")
    String purpose
) {}
