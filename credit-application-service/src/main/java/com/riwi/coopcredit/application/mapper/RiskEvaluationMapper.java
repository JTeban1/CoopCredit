package com.riwi.coopcredit.application.mapper;

import com.riwi.coopcredit.application.dto.response.RiskEvaluationResponse;
import com.riwi.coopcredit.domain.model.RiskEvaluation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RiskEvaluationMapper {

    RiskEvaluationResponse toResponse(RiskEvaluation evaluation);
}
