package com.riwi.coopcredit.domain.service;

import com.riwi.coopcredit.domain.model.Affiliate;
import com.riwi.coopcredit.domain.port.in.RegisterAffiliateUseCase;
import com.riwi.coopcredit.domain.port.out.AffiliateRepositoryPort;
import com.riwi.coopcredit.infrastructure.exception.BusinessException;
import com.riwi.coopcredit.infrastructure.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class RegisterAffiliateService implements RegisterAffiliateUseCase {

    private final AffiliateRepositoryPort affiliateRepository;

    public RegisterAffiliateService(AffiliateRepositoryPort affiliateRepository) {
        this.affiliateRepository = affiliateRepository;
    }

    @Override
    public Affiliate register(Affiliate affiliate) {
        validateAffiliateDoesNotExist(affiliate);
        
        affiliate.setAffiliationDate(LocalDate.now());
        affiliate.setActive(true);
        
        return affiliateRepository.save(affiliate);
    }

    private void validateAffiliateDoesNotExist(Affiliate affiliate) {
        if (affiliateRepository.existsByDocument(affiliate.getDocument())) {
            throw new BusinessException(ErrorCode.AFFILIATE_ALREADY_EXISTS, 
                "An affiliate with document " + affiliate.getDocument() + " already exists");
        }
        
        if (affiliateRepository.existsByEmail(affiliate.getEmail())) {
            throw new BusinessException(ErrorCode.AFFILIATE_ALREADY_EXISTS, 
                "An affiliate with email " + affiliate.getEmail() + " already exists");
        }
    }
}
