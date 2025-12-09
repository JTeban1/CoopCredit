package com.riwi.riskcentral.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluationResponse {
    private String document;
    private Long creditApplicationId;
    private Integer riskScore;
    private String riskLevel;
    private String evaluationDetails;
    private LocalDateTime evaluationDate;
}
