package com.riwi.coopcredit.domain.port.out;

import com.riwi.coopcredit.domain.model.RiskEvaluation;

import java.util.Optional;

public interface RiskEvaluationRepositoryPort {
    RiskEvaluation save(RiskEvaluation riskEvaluation);
    Optional<RiskEvaluation> findById(Long id);
    Optional<RiskEvaluation> findByCreditApplicationId(Long creditApplicationId);
}
