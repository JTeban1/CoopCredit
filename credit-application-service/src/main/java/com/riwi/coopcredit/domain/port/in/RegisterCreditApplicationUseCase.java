package com.riwi.coopcredit.domain.port.in;

import com.riwi.coopcredit.domain.model.CreditApplication;

public interface RegisterCreditApplicationUseCase {
    CreditApplication register(CreditApplication creditApplication);
}
