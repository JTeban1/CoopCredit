package com.riwi.coopcredit.domain.port.out;

import com.riwi.coopcredit.domain.model.RiskEvaluation;

public interface RiskEvaluationPort {
    RiskEvaluation evaluateRisk(String document, Long creditApplicationId);
}
