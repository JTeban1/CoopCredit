package com.riwi.coopcredit.infrastructure.adapter.out.persistence.repository;

import com.riwi.coopcredit.infrastructure.adapter.out.persistence.entity.AffiliateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AffiliateJpaRepository extends JpaRepository<AffiliateEntity, Long> {
    Optional<AffiliateEntity> findByDocument(String document);
    boolean existsByDocument(String document);
    boolean existsByEmail(String email);
    Optional<AffiliateEntity> findByEmail(String email);
}
