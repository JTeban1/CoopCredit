package com.riwi.coopcredit.domain.port.out;

import com.riwi.coopcredit.domain.model.Affiliate;

import java.util.Optional;

public interface AffiliateRepositoryPort {
    Affiliate save(Affiliate affiliate);
    Optional<Affiliate> findById(Long id);
    Optional<Affiliate> findByDocument(String document);
    boolean existsByDocument(String document);
    boolean existsByEmail(String email);
}
