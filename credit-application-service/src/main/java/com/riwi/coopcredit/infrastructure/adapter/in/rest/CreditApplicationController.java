package com.riwi.coopcredit.infrastructure.adapter.in.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.riwi.coopcredit.application.dto.request.CreditApplicationRequest;
import com.riwi.coopcredit.application.dto.response.CreditApplicationResponse;
import com.riwi.coopcredit.application.mapper.CreditApplicationMapper;
import com.riwi.coopcredit.domain.model.CreditApplication;
import com.riwi.coopcredit.domain.port.in.EvaluateCreditApplicationUseCase;
import com.riwi.coopcredit.domain.port.in.RegisterCreditApplicationUseCase;
import com.riwi.coopcredit.domain.port.out.CreditApplicationRepositoryPort;
import com.riwi.coopcredit.infrastructure.exception.BusinessException;
import com.riwi.coopcredit.infrastructure.exception.ErrorCode;

import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/applications")
public class CreditApplicationController {

    private final RegisterCreditApplicationUseCase registerCreditApplicationUseCase;
    private final EvaluateCreditApplicationUseCase evaluateCreditApplicationUseCase;
    private final CreditApplicationRepositoryPort creditApplicationRepository;
    private final CreditApplicationMapper creditApplicationMapper;

    public CreditApplicationController(
            RegisterCreditApplicationUseCase registerCreditApplicationUseCase,
            EvaluateCreditApplicationUseCase evaluateCreditApplicationUseCase,
            CreditApplicationRepositoryPort creditApplicationRepository,
            CreditApplicationMapper creditApplicationMapper) {
        this.registerCreditApplicationUseCase = registerCreditApplicationUseCase;
        this.evaluateCreditApplicationUseCase = evaluateCreditApplicationUseCase;
        this.creditApplicationRepository = creditApplicationRepository;
        this.creditApplicationMapper = creditApplicationMapper;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALISTA', 'AFILIADO')")
    @Timed(value = "credit.application.create", description = "Time to create a credit application")
    public ResponseEntity<CreditApplicationResponse> createApplication(
            @Valid @RequestBody CreditApplicationRequest request) {
        CreditApplication application = creditApplicationMapper.toDomain(request);
        CreditApplication created = registerCreditApplicationUseCase.register(application);
        CreditApplicationResponse response = creditApplicationMapper.toResponse(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALISTA', 'AFILIADO')")
    @Timed(value = "credit.application.get", description = "Time to get a credit application")
    public ResponseEntity<CreditApplicationResponse> getApplication(@PathVariable Long id) {
        CreditApplication application = creditApplicationRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.CREDIT_APPLICATION_NOT_FOUND,
                "Credit application not found with ID: " + id));
        return ResponseEntity.ok(creditApplicationMapper.toResponse(application));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALISTA')")
    @Timed(value = "credit.application.getAll", description = "Time to get all credit applications")
    public ResponseEntity<List<CreditApplicationResponse>> getAllApplications() {
        List<CreditApplicationResponse> applications = creditApplicationRepository
            .findAll().stream()
            .map(creditApplicationMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/affiliate/{affiliateId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALISTA', 'AFILIADO')")
    @Timed(value = "credit.application.getByAffiliate", description = "Time to get applications by affiliate")
    public ResponseEntity<List<CreditApplicationResponse>> getApplicationsByAffiliate(
            @PathVariable Long affiliateId) {
        List<CreditApplicationResponse> applications = creditApplicationRepository
            .findByAffiliateId(affiliateId).stream()
            .map(creditApplicationMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(applications);
    }

    @PostMapping("/{id}/evaluate")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALISTA')")
    @Timed(value = "credit.application.evaluate", description = "Time to evaluate a credit application")
    public ResponseEntity<CreditApplicationResponse> evaluateApplication(@PathVariable Long id) {
        CreditApplication evaluated = evaluateCreditApplicationUseCase.evaluate(id);
        return ResponseEntity.ok(creditApplicationMapper.toResponse(evaluated));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALISTA')")
    @Timed(value = "credit.application.getByStatus", description = "Time to get applications by status")
    public ResponseEntity<List<CreditApplicationResponse>> getApplicationsByStatus(
            @PathVariable String status) {
        CreditApplication.ApplicationStatus appStatus = 
            CreditApplication.ApplicationStatus.valueOf(status.toUpperCase());
        List<CreditApplicationResponse> applications = creditApplicationRepository
            .findByStatus(appStatus).stream()
            .map(creditApplicationMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(applications);
    }
}
