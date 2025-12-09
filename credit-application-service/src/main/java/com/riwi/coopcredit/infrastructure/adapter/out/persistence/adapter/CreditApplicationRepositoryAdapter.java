package com.riwi.coopcredit.infrastructure.adapter.out.persistence.adapter;

import com.riwi.coopcredit.domain.model.CreditApplication;
import com.riwi.coopcredit.domain.port.out.CreditApplicationRepositoryPort;
import com.riwi.coopcredit.infrastructure.adapter.out.persistence.entity.AffiliateEntity;
import com.riwi.coopcredit.infrastructure.adapter.out.persistence.entity.CreditApplicationEntity;
import com.riwi.coopcredit.infrastructure.adapter.out.persistence.repository.AffiliateJpaRepository;
import com.riwi.coopcredit.infrastructure.adapter.out.persistence.repository.CreditApplicationJpaRepository;
import com.riwi.coopcredit.infrastructure.exception.BusinessException;
import com.riwi.coopcredit.infrastructure.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CreditApplicationRepositoryAdapter implements CreditApplicationRepositoryPort {

    private final CreditApplicationJpaRepository jpaRepository;
    private final AffiliateJpaRepository affiliateJpaRepository;

    public CreditApplicationRepositoryAdapter(CreditApplicationJpaRepository jpaRepository,
                                              AffiliateJpaRepository affiliateJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.affiliateJpaRepository = affiliateJpaRepository;
    }

    @Override
    public CreditApplication save(CreditApplication application) {
        CreditApplicationEntity entity = toEntity(application);
        CreditApplicationEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<CreditApplication> findById(Long id) {
        return jpaRepository.findWithRiskEvaluationById(id).map(this::toDomain);
    }

    @Override
    public List<CreditApplication> findByAffiliateId(Long affiliateId) {
        return jpaRepository.findByAffiliateId(affiliateId).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<CreditApplication> findByStatus(CreditApplication.ApplicationStatus status) {
        CreditApplicationEntity.ApplicationStatus entityStatus = 
            CreditApplicationEntity.ApplicationStatus.valueOf(status.name());
        return jpaRepository.findByStatus(entityStatus).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    private CreditApplicationEntity toEntity(CreditApplication application) {
        AffiliateEntity affiliate = affiliateJpaRepository.findById(application.getAffiliateId())
            .orElseThrow(() -> new BusinessException(ErrorCode.AFFILIATE_NOT_FOUND,
                "Affiliate not found with ID: " + application.getAffiliateId()));

        CreditApplicationEntity.ApplicationStatus status = application.getStatus() != null
            ? CreditApplicationEntity.ApplicationStatus.valueOf(application.getStatus().name())
            : CreditApplicationEntity.ApplicationStatus.PENDING;

        CreditApplicationEntity entity = CreditApplicationEntity.builder()
            .id(application.getId())
            .affiliate(affiliate)
            .requestedAmount(application.getRequestedAmount())
            .termMonths(application.getTermMonths())
            .purpose(application.getPurpose())
            .status(status)
            .applicationDate(application.getApplicationDate())
            .build();

        return entity;
    }

    private CreditApplication toDomain(CreditApplicationEntity entity) {
        CreditApplication application = new CreditApplication();
        application.setId(entity.getId());
        application.setAffiliateId(entity.getAffiliate().getId());
        application.setRequestedAmount(entity.getRequestedAmount());
        application.setTermMonths(entity.getTermMonths());
        application.setPurpose(entity.getPurpose());
        application.setStatus(CreditApplication.ApplicationStatus.valueOf(entity.getStatus().name()));
        application.setApplicationDate(entity.getApplicationDate());
        return application;
    }
}
