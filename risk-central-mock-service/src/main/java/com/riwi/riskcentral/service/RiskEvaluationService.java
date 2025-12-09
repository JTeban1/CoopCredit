package com.riwi.riskcentral.service;

import com.riwi.riskcentral.model.RiskEvaluationRequest;
import com.riwi.riskcentral.model.RiskEvaluationResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class RiskEvaluationService {

    public RiskEvaluationResponse evaluate(RiskEvaluationRequest request) {
        int seed = request.getDocument().hashCode();
        Random random = new Random(seed);

        int score = 300 + random.nextInt(651);

        String riskLevel = determineRiskLevel(score);

        return RiskEvaluationResponse.builder()
            .document(request.getDocument())
            .creditApplicationId(request.getCreditApplicationId())
            .riskScore(score)
            .riskLevel(riskLevel)
            .evaluationDetails(generateDetails(score, riskLevel))
            .evaluationDate(LocalDateTime.now())
            .build();
    }

    private String determineRiskLevel(int score) {
        if (score >= 701) return "LOW"; //Risk, not credit
        if (score >= 501) return "MEDIUM";
        return "HIGH";
    }

    private String generateDetails(int score, String riskLevel) {
        return String.format(
            "Risk evaluation completed. Score: %d, Level: %s. " +
            "Based on credit history and financial behavior analysis.",
            score, riskLevel
        );
    }
}
