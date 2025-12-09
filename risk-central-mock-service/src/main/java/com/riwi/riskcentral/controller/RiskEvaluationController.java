package com.riwi.riskcentral.controller;

import com.riwi.riskcentral.model.RiskEvaluationRequest;
import com.riwi.riskcentral.model.RiskEvaluationResponse;
import com.riwi.riskcentral.service.RiskEvaluationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/risk-evaluation")
@RequiredArgsConstructor
@Slf4j
public class RiskEvaluationController {

    private final RiskEvaluationService riskEvaluationService;

    @PostMapping
    public ResponseEntity<RiskEvaluationResponse> evaluateRisk(
            @RequestBody RiskEvaluationRequest request) {
        log.info("Received risk evaluation request for document: {}, applicationId: {}",
            request.getDocument(), request.getCreditApplicationId());

        RiskEvaluationResponse response = riskEvaluationService.evaluate(request);

        log.info("Risk evaluation completed. Score: {}, Level: {}",
            response.getRiskScore(), response.getRiskLevel());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Risk Central Mock Service is running");
    }
}
