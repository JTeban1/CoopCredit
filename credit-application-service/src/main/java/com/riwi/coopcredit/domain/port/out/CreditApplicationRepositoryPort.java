package com.riwi.coopcredit.domain.port.out;

import com.riwi.coopcredit.domain.model.CreditApplication;

import java.util.List;
import java.util.Optional;

public interface CreditApplicationRepositoryPort {
    CreditApplication save(CreditApplication application);
    Optional<CreditApplication> findById(Long id);
    List<CreditApplication> findByAffiliateId(Long affiliateId);
    List<CreditApplication> findByStatus(CreditApplication.ApplicationStatus status);
}
