package com.riwi.coopcredit.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.riwi.coopcredit.domain.model.CreditApplication;

public interface CreditApplicationRepositoryPort {
    CreditApplication save(CreditApplication application);
    Optional<CreditApplication> findById(Long id);
    List<CreditApplication> findAll();
    List<CreditApplication> findByAffiliateId(Long affiliateId);
    List<CreditApplication> findByStatus(CreditApplication.ApplicationStatus status);
}
