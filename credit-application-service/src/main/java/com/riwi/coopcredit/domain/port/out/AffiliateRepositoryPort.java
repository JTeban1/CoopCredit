package com.riwi.coopcredit.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.riwi.coopcredit.domain.model.Affiliate;

public interface AffiliateRepositoryPort {
    Affiliate save(Affiliate affiliate);
    Optional<Affiliate> findById(Long id);
    Optional<Affiliate> findByDocument(String document);
    List<Affiliate> findAll();
    boolean existsByDocument(String document);
    boolean existsByEmail(String email);
}
