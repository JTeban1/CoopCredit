package com.riwi.riskcentral.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluationRequest {
    private String document;
    private Long creditApplicationId;
}
