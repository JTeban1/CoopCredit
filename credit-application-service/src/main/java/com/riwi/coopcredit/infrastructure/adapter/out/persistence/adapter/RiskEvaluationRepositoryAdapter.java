package com.riwi.coopcredit.infrastructure.adapter.out.persistence.adapter;

import com.riwi.coopcredit.domain.model.RiskEvaluation;
import com.riwi.coopcredit.domain.port.out.RiskEvaluationRepositoryPort;
import com.riwi.coopcredit.infrastructure.adapter.out.persistence.entity.CreditApplicationEntity;
import com.riwi.coopcredit.infrastructure.adapter.out.persistence.entity.RiskEvaluationEntity;
import com.riwi.coopcredit.infrastructure.adapter.out.persistence.repository.CreditApplicationJpaRepository;
import com.riwi.coopcredit.infrastructure.adapter.out.persistence.repository.RiskEvaluationJpaRepository;
import com.riwi.coopcredit.infrastructure.exception.BusinessException;
import com.riwi.coopcredit.infrastructure.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RiskEvaluationRepositoryAdapter implements RiskEvaluationRepositoryPort {

    private final RiskEvaluationJpaRepository jpaRepository;
    private final CreditApplicationJpaRepository creditApplicationJpaRepository;

    public RiskEvaluationRepositoryAdapter(RiskEvaluationJpaRepository jpaRepository,
                                           CreditApplicationJpaRepository creditApplicationJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.creditApplicationJpaRepository = creditApplicationJpaRepository;
    }

    @Override
    public RiskEvaluation save(RiskEvaluation riskEvaluation) {
        RiskEvaluationEntity entity = toEntity(riskEvaluation);
        RiskEvaluationEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<RiskEvaluation> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<RiskEvaluation> findByCreditApplicationId(Long creditApplicationId) {
        return jpaRepository.findByCreditApplicationId(creditApplicationId).map(this::toDomain);
    }

    private RiskEvaluationEntity toEntity(RiskEvaluation evaluation) {
        CreditApplicationEntity creditApplication = creditApplicationJpaRepository
            .findById(evaluation.getCreditApplicationId())
            .orElseThrow(() -> new BusinessException(ErrorCode.CREDIT_APPLICATION_NOT_FOUND,
                "Credit application not found with ID: " + evaluation.getCreditApplicationId()));

        return RiskEvaluationEntity.builder()
            .id(evaluation.getId())
            .creditApplication(creditApplication)
            .riskScore(evaluation.getRiskScore())
            .riskLevel(evaluation.getRiskLevel())
            .evaluationDetails(evaluation.getEvaluationDetails())
            .evaluationDate(evaluation.getEvaluationDate())
            .build();
    }

    private RiskEvaluation toDomain(RiskEvaluationEntity entity) {
        RiskEvaluation evaluation = new RiskEvaluation();
        evaluation.setId(entity.getId());
        evaluation.setCreditApplicationId(entity.getCreditApplication().getId());
        evaluation.setRiskScore(entity.getRiskScore());
        evaluation.setRiskLevel(entity.getRiskLevel());
        evaluation.setEvaluationDetails(entity.getEvaluationDetails());
        evaluation.setEvaluationDate(entity.getEvaluationDate());
        return evaluation;
    }
}
