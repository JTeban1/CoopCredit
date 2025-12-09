package com.riwi.coopcredit.infrastructure.adapter.out.persistence.repository;

import com.riwi.coopcredit.infrastructure.adapter.out.persistence.entity.RiskEvaluationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiskEvaluationJpaRepository extends JpaRepository<RiskEvaluationEntity, Long> {
    Optional<RiskEvaluationEntity> findByCreditApplicationId(Long creditApplicationId);
}
