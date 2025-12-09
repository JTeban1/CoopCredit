package com.riwi.coopcredit.domain.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.riwi.coopcredit.domain.model.Affiliate;
import com.riwi.coopcredit.domain.model.CreditApplication;
import com.riwi.coopcredit.domain.port.out.AffiliateRepositoryPort;
import com.riwi.coopcredit.domain.port.out.CreditApplicationRepositoryPort;
import com.riwi.coopcredit.infrastructure.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
class RegisterCreditApplicationServiceTest {

    @Mock
    private CreditApplicationRepositoryPort creditApplicationRepository;

    @Mock
    private AffiliateRepositoryPort affiliateRepository;

    @InjectMocks
    private RegisterCreditApplicationService service;

    private Affiliate activeAffiliate;
    private CreditApplication validApplication;

    @BeforeEach
    void setUp() {
        activeAffiliate = new Affiliate();
        activeAffiliate.setId(1L);
        activeAffiliate.setDocument("123456789");
        activeAffiliate.setSalary(new BigDecimal("3000000"));
        activeAffiliate.setAffiliationDate(LocalDate.now().minusYears(1));
        activeAffiliate.setActive(true);

        validApplication = new CreditApplication();
        validApplication.setAffiliateId(1L);
        validApplication.setRequestedAmount(new BigDecimal("10000000"));
        validApplication.setTermMonths(24);
        validApplication.setPurpose("Home improvement");
    }

    @Test
    @DisplayName("Should register credit application successfully when all validations pass")
    void shouldRegisterCreditApplicationSuccessfully() {
        when(affiliateRepository.findById(1L)).thenReturn(Optional.of(activeAffiliate));
        when(creditApplicationRepository.save(any(CreditApplication.class)))
            .thenAnswer(invocation -> {
                CreditApplication app = invocation.getArgument(0);
                app.setId(1L);
                return app;
            });

        CreditApplication result = service.register(validApplication);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(CreditApplication.ApplicationStatus.PENDING, result.getStatus());
        assertNotNull(result.getApplicationDate());
        verify(creditApplicationRepository).save(any(CreditApplication.class));
    }

    @Test
    @DisplayName("Should throw exception when affiliate is not found")
    void shouldThrowExceptionWhenAffiliateNotFound() {
        when(affiliateRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> service.register(validApplication));
        verify(creditApplicationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when affiliate is not active")
    void shouldThrowExceptionWhenAffiliateNotActive() {
        activeAffiliate.setActive(false);
        when(affiliateRepository.findById(1L)).thenReturn(Optional.of(activeAffiliate));

        assertThrows(BusinessException.class, () -> service.register(validApplication));
        verify(creditApplicationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when affiliate has insufficient seniority")
    void shouldThrowExceptionWhenInsufficientSeniority() {
        activeAffiliate.setAffiliationDate(LocalDate.now().minusMonths(3));
        when(affiliateRepository.findById(1L)).thenReturn(Optional.of(activeAffiliate));

        assertThrows(BusinessException.class, () -> service.register(validApplication));
        verify(creditApplicationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when requested amount exceeds maximum")
    void shouldThrowExceptionWhenAmountExceedsMaximum() {
        validApplication.setRequestedAmount(new BigDecimal("20000000"));
        when(affiliateRepository.findById(1L)).thenReturn(Optional.of(activeAffiliate));

        assertThrows(BusinessException.class, () -> service.register(validApplication));
        verify(creditApplicationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when monthly payment exceeds 40% of salary")
    void shouldThrowExceptionWhenInstallmentExceedsRatio() {
        // Salary: 3,000,000, 40% = 1,200,000
        // To exceed 40%: monthly payment > 1,200,000
        // 15,000,000 / 12 = 1,250,000 (41.67%)
        validApplication.setRequestedAmount(new BigDecimal("15000000"));
        validApplication.setTermMonths(12);
        when(affiliateRepository.findById(1L)).thenReturn(Optional.of(activeAffiliate));

        assertThrows(BusinessException.class, () -> service.register(validApplication));
        verify(creditApplicationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when term months is zero or negative")
    void shouldThrowExceptionWhenInvalidTermMonths() {
        validApplication.setTermMonths(0);
        when(affiliateRepository.findById(1L)).thenReturn(Optional.of(activeAffiliate));

        assertThrows(BusinessException.class, () -> service.register(validApplication));
        verify(creditApplicationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when term months exceeds maximum")
    void shouldThrowExceptionWhenTermExceedsMaximum() {
        validApplication.setTermMonths(72);
        when(affiliateRepository.findById(1L)).thenReturn(Optional.of(activeAffiliate));

        assertThrows(BusinessException.class, () -> service.register(validApplication));
        verify(creditApplicationRepository, never()).save(any());
    }
}
