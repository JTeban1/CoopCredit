package com.riwi.coopcredit.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.riwi.coopcredit.domain.model.Affiliate;
import com.riwi.coopcredit.domain.model.CreditApplication;
import com.riwi.coopcredit.domain.port.in.RegisterCreditApplicationUseCase;
import com.riwi.coopcredit.domain.port.out.AffiliateRepositoryPort;
import com.riwi.coopcredit.domain.port.out.CreditApplicationRepositoryPort;
import com.riwi.coopcredit.infrastructure.exception.BusinessException;
import com.riwi.coopcredit.infrastructure.exception.ErrorCode;

@Service
@Transactional
public class RegisterCreditApplicationService implements RegisterCreditApplicationUseCase {

    private final CreditApplicationRepositoryPort creditApplicationRepository;
    private final AffiliateRepositoryPort affiliateRepository;

    private static final BigDecimal MAX_INSTALLMENT_RATIO = new BigDecimal("0.40");
    private static final int MIN_SENIORITY_MONTHS = 6;

    public RegisterCreditApplicationService(CreditApplicationRepositoryPort creditApplicationRepository,
                                            AffiliateRepositoryPort affiliateRepository) {
        this.creditApplicationRepository = creditApplicationRepository;
        this.affiliateRepository = affiliateRepository;
    }

    @Override
    public CreditApplication register(CreditApplication creditApplication) {
        Affiliate affiliate = affiliateRepository.findById(creditApplication.getAffiliateId())
            .orElseThrow(() -> new BusinessException(ErrorCode.AFFILIATE_NOT_FOUND, 
                "Affiliate not found with ID: " + creditApplication.getAffiliateId()));

        if (!affiliate.canApplyForCredit()) {
            throw new BusinessException(ErrorCode.AFFILIATE_NOT_ACTIVE, 
                "Affiliate is not active and cannot apply for credit");
        }

        validateSeniority(affiliate.getAffiliationDate());
        validateCreditAmount(creditApplication.getRequestedAmount(), affiliate);
        validateInstallmentRatio(creditApplication, affiliate.getSalary());
        validateTermMonths(creditApplication.getTermMonths());

        creditApplication.setStatus(CreditApplication.ApplicationStatus.PENDING);
        creditApplication.setApplicationDate(LocalDateTime.now());

        return creditApplicationRepository.save(creditApplication);
    }

    private void validateSeniority(LocalDate affiliationDate) {
        Period period = Period.between(affiliationDate, LocalDate.now());
        int months = period.getYears() * 12 + period.getMonths();

        if (months < MIN_SENIORITY_MONTHS) {
            throw new BusinessException(ErrorCode.INVALID_CREDIT_AMOUNT,
                "Minimum seniority of " + MIN_SENIORITY_MONTHS + " months required. Current: " + months + " months");
        }
    }

    private void validateCreditAmount(BigDecimal requestedAmount, Affiliate affiliate) {
        if (requestedAmount == null || requestedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCode.INVALID_CREDIT_AMOUNT,
                "Requested amount must be greater than zero");
        }

        BigDecimal maxAmount = affiliate.getMaxCreditAmount();
        if (requestedAmount.compareTo(maxAmount) > 0) {
            throw new BusinessException(ErrorCode.INVALID_CREDIT_AMOUNT,
                "Requested amount exceeds maximum allowed: " + maxAmount);
        }
    }

    private void validateInstallmentRatio(CreditApplication application, BigDecimal salary) {
        BigDecimal monthlyPayment = application.calculateMonthlyPayment();
        BigDecimal ratio = monthlyPayment.divide(salary, 4, RoundingMode.HALF_UP);

        if (ratio.compareTo(MAX_INSTALLMENT_RATIO) > 0) {
            throw new BusinessException(ErrorCode.INVALID_CREDIT_AMOUNT,
                "Monthly installment exceeds 40% of salary. Ratio: " + 
                ratio.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP) + "%");
        }
    }

    private void validateTermMonths(Integer termMonths) {
        if (termMonths == null || termMonths <= 0) {
            throw new BusinessException(ErrorCode.INVALID_TERM,
                "Term months must be greater than zero");
        }
        
        if (termMonths > 60) {
            throw new BusinessException(ErrorCode.INVALID_TERM,
                "Term months cannot exceed 60 months");
        }
    }
}
