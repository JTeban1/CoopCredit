package com.riwi.coopcredit.infrastructure.adapter.in.rest;

import com.riwi.coopcredit.application.dto.request.AffiliateRequest;
import com.riwi.coopcredit.application.dto.response.AffiliateResponse;
import com.riwi.coopcredit.application.mapper.AffiliateMapper;
import com.riwi.coopcredit.domain.model.Affiliate;
import com.riwi.coopcredit.domain.port.in.RegisterAffiliateUseCase;
import com.riwi.coopcredit.domain.port.out.AffiliateRepositoryPort;
import com.riwi.coopcredit.infrastructure.exception.BusinessException;
import com.riwi.coopcredit.infrastructure.exception.ErrorCode;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/affiliates")
public class AffiliateController {

    private final RegisterAffiliateUseCase registerAffiliateUseCase;
    private final AffiliateRepositoryPort affiliateRepository;
    private final AffiliateMapper affiliateMapper;

    public AffiliateController(RegisterAffiliateUseCase registerAffiliateUseCase,
                               AffiliateRepositoryPort affiliateRepository,
                               AffiliateMapper affiliateMapper) {
        this.registerAffiliateUseCase = registerAffiliateUseCase;
        this.affiliateRepository = affiliateRepository;
        this.affiliateMapper = affiliateMapper;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALISTA')")
    @Timed(value = "affiliate.register", description = "Time to register an affiliate")
    public ResponseEntity<AffiliateResponse> registerAffiliate(
            @Valid @RequestBody AffiliateRequest request) {
        Affiliate affiliate = affiliateMapper.toDomain(request);
        Affiliate registered = registerAffiliateUseCase.register(affiliate);
        AffiliateResponse response = affiliateMapper.toResponse(registered);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALISTA', 'AFILIADO')")
    @Timed(value = "affiliate.get", description = "Time to get an affiliate")
    public ResponseEntity<AffiliateResponse> getAffiliate(@PathVariable Long id) {
        Affiliate affiliate = affiliateRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.AFFILIATE_NOT_FOUND,
                "Affiliate not found with ID: " + id));
        return ResponseEntity.ok(affiliateMapper.toResponse(affiliate));
    }

    @GetMapping("/document/{document}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALISTA')")
    @Timed(value = "affiliate.getByDocument", description = "Time to get an affiliate by document")
    public ResponseEntity<AffiliateResponse> getAffiliateByDocument(@PathVariable String document) {
        Affiliate affiliate = affiliateRepository.findByDocument(document)
            .orElseThrow(() -> new BusinessException(ErrorCode.AFFILIATE_NOT_FOUND,
                "Affiliate not found with document: " + document));
        return ResponseEntity.ok(affiliateMapper.toResponse(affiliate));
    }
}
