package com.riwi.coopcredit.infrastructure.adapter.out.external;

import com.riwi.coopcredit.domain.model.RiskEvaluation;
import com.riwi.coopcredit.domain.port.out.RiskEvaluationPort;
import com.riwi.coopcredit.infrastructure.exception.BusinessException;
import com.riwi.coopcredit.infrastructure.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class RiskCentralAdapter implements RiskEvaluationPort {

    private final RestClient restClient;
    private final String riskCentralUrl;

    public RiskCentralAdapter(RestClient restClient,
                              @Value("${risk-central.url}") String riskCentralUrl) {
        this.restClient = restClient;
        this.riskCentralUrl = riskCentralUrl;
    }

    @Override
    public RiskEvaluation evaluateRisk(String document, Long creditApplicationId) {
        try {
            var request = Map.of(
                "document", document,
                "creditApplicationId", creditApplicationId
            );

            RiskEvaluationResponse response = restClient.post()
                .uri(riskCentralUrl + "/api/risk-evaluation")
                .body(request)
                .retrieve()
                .body(RiskEvaluationResponse.class);

            if (response == null) {
                throw new BusinessException(ErrorCode.RISK_EVALUATION_FAILED,
                    "No response received from risk evaluation service");
            }

            return mapToRiskEvaluation(response, creditApplicationId);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.RISK_EVALUATION_FAILED,
                "Failed to evaluate risk: " + e.getMessage());
        }
    }

    private RiskEvaluation mapToRiskEvaluation(RiskEvaluationResponse response, Long creditApplicationId) {
        RiskEvaluation evaluation = new RiskEvaluation();
        evaluation.setCreditApplicationId(creditApplicationId);
        evaluation.setRiskScore(response.riskScore());
        evaluation.setRiskLevel(response.riskLevel());
        evaluation.setEvaluationDetails(response.evaluationDetails());
        evaluation.setEvaluationDate(response.evaluationDate() != null 
            ? response.evaluationDate() 
            : LocalDateTime.now());
        return evaluation;
    }

    private record RiskEvaluationResponse(
        String document,
        Long creditApplicationId,
        Integer riskScore,
        String riskLevel,
        String evaluationDetails,
        LocalDateTime evaluationDate
    ) {}
}
