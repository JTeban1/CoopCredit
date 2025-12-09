package com.riwi.coopcredit.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AffiliateResponse(
    Long id,
    String document,
    String firstName,
    String lastName,
    String email,
    String phone,
    BigDecimal salary,
    LocalDate affiliationDate,
    boolean active,
    BigDecimal maxCreditAmount
) {}
