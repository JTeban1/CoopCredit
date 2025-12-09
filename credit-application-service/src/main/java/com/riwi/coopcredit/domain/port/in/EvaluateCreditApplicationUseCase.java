package com.riwi.coopcredit.domain.port.in;

import com.riwi.coopcredit.domain.model.CreditApplication;

public interface EvaluateCreditApplicationUseCase {
    CreditApplication evaluate(Long creditApplicationId);
}
