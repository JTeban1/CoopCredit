package com.riwi.coopcredit.domain.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.riwi.coopcredit.domain.model.Affiliate;
import com.riwi.coopcredit.domain.model.CreditApplication;
import com.riwi.coopcredit.domain.model.RiskEvaluation;
import com.riwi.coopcredit.domain.port.in.EvaluateCreditApplicationUseCase;
import com.riwi.coopcredit.domain.port.out.AffiliateRepositoryPort;
import com.riwi.coopcredit.domain.port.out.CreditApplicationRepositoryPort;
import com.riwi.coopcredit.domain.port.out.RiskEvaluationPort;
import com.riwi.coopcredit.domain.port.out.RiskEvaluationRepositoryPort;
import com.riwi.coopcredit.infrastructure.exception.BusinessException;
import com.riwi.coopcredit.infrastructure.exception.ErrorCode;

import io.micrometer.core.instrument.Counter;

@Service
@Transactional
public class EvaluateCreditApplicationService implements EvaluateCreditApplicationUseCase {

    private final CreditApplicationRepositoryPort creditApplicationRepository;
    private final AffiliateRepositoryPort affiliateRepository;
    private final RiskEvaluationPort riskEvaluationPort;
    private final RiskEvaluationRepositoryPort riskEvaluationRepository;
    private final Counter creditApplicationCounter;

    public EvaluateCreditApplicationService(
            CreditApplicationRepositoryPort creditApplicationRepository,
            AffiliateRepositoryPort affiliateRepository,
            RiskEvaluationPort riskEvaluationPort,
            RiskEvaluationRepositoryPort riskEvaluationRepository,
            @Qualifier("creditApplicationCounter") Counter creditApplicationCounter) {
        this.creditApplicationRepository = creditApplicationRepository;
        this.affiliateRepository = affiliateRepository;
        this.riskEvaluationPort = riskEvaluationPort;
        this.riskEvaluationRepository = riskEvaluationRepository;
        this.creditApplicationCounter = creditApplicationCounter;
    }

    @Override
    public CreditApplication evaluate(Long creditApplicationId) {
        CreditApplication application = creditApplicationRepository.findById(creditApplicationId)
            .orElseThrow(() -> new BusinessException(ErrorCode.CREDIT_APPLICATION_NOT_FOUND,
                "Credit application not found with ID: " + creditApplicationId));

        if (application.getStatus() != CreditApplication.ApplicationStatus.PENDING) {
            throw new BusinessException(ErrorCode.INVALID_CREDIT_AMOUNT,
                "Only pending applications can be evaluated");
        }

        application.setStatus(CreditApplication.ApplicationStatus.IN_EVALUATION);
        creditApplicationRepository.save(application);

        Affiliate affiliate = affiliateRepository.findById(application.getAffiliateId())
            .orElseThrow(() -> new BusinessException(ErrorCode.AFFILIATE_NOT_FOUND,
                "Affiliate not found with ID: " + application.getAffiliateId()));

        RiskEvaluation riskEvaluation = riskEvaluationPort.evaluateRisk(
            affiliate.getDocument(), 
            creditApplicationId
        );

        riskEvaluation = riskEvaluationRepository.save(riskEvaluation);

        if (riskEvaluation.isApproved()) {
            application.setStatus(CreditApplication.ApplicationStatus.APPROVED);
        } else {
            application.setStatus(CreditApplication.ApplicationStatus.REJECTED);
        }

        application.setRiskEvaluation(riskEvaluation);
        CreditApplication savedApplication = creditApplicationRepository.save(application);

        creditApplicationCounter.increment();

        return savedApplication;
    }
}
