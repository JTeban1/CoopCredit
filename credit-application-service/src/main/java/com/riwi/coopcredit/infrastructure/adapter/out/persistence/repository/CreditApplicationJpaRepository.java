package com.riwi.coopcredit.infrastructure.adapter.out.persistence.repository;

import com.riwi.coopcredit.infrastructure.adapter.out.persistence.entity.CreditApplicationEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditApplicationJpaRepository extends JpaRepository<CreditApplicationEntity, Long> {

    @EntityGraph(value = "CreditApplication.withRiskEvaluation")
    Optional<CreditApplicationEntity> findWithRiskEvaluationById(Long id);

    List<CreditApplicationEntity> findByAffiliateId(Long affiliateId);
    
    List<CreditApplicationEntity> findByStatus(CreditApplicationEntity.ApplicationStatus status);
}
