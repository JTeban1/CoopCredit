package com.riwi.coopcredit.infrastructure.adapter.out.persistence.adapter;

import com.riwi.coopcredit.domain.model.Affiliate;
import com.riwi.coopcredit.domain.port.out.AffiliateRepositoryPort;
import com.riwi.coopcredit.infrastructure.adapter.out.persistence.entity.AffiliateEntity;
import com.riwi.coopcredit.infrastructure.adapter.out.persistence.repository.AffiliateJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AffiliateRepositoryAdapter implements AffiliateRepositoryPort {

    private final AffiliateJpaRepository jpaRepository;

    public AffiliateRepositoryAdapter(AffiliateJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Affiliate save(Affiliate affiliate) {
        AffiliateEntity entity = toEntity(affiliate);
        AffiliateEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Affiliate> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Affiliate> findByDocument(String document) {
        return jpaRepository.findByDocument(document).map(this::toDomain);
    }

    @Override
    public boolean existsByDocument(String document) {
        return jpaRepository.existsByDocument(document);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    private AffiliateEntity toEntity(Affiliate affiliate) {
        return AffiliateEntity.builder()
            .id(affiliate.getId())
            .document(affiliate.getDocument())
            .firstName(affiliate.getFirstName())
            .lastName(affiliate.getLastName())
            .email(affiliate.getEmail())
            .phone(affiliate.getPhone())
            .salary(affiliate.getSalary())
            .affiliationDate(affiliate.getAffiliationDate())
            .active(affiliate.isActive())
            .build();
    }

    private Affiliate toDomain(AffiliateEntity entity) {
        Affiliate affiliate = new Affiliate();
        affiliate.setId(entity.getId());
        affiliate.setDocument(entity.getDocument());
        affiliate.setFirstName(entity.getFirstName());
        affiliate.setLastName(entity.getLastName());
        affiliate.setEmail(entity.getEmail());
        affiliate.setPhone(entity.getPhone());
        affiliate.setSalary(entity.getSalary());
        affiliate.setAffiliationDate(entity.getAffiliationDate());
        affiliate.setActive(entity.isActive());
        return affiliate;
    }
}
