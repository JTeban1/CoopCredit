package com.riwi.coopcredit.application.mapper;

import com.riwi.coopcredit.application.dto.request.CreditApplicationRequest;
import com.riwi.coopcredit.application.dto.response.CreditApplicationResponse;
import com.riwi.coopcredit.domain.model.CreditApplication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RiskEvaluationMapper.class})
public interface CreditApplicationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "applicationDate", ignore = true)
    @Mapping(target = "riskEvaluation", ignore = true)
    CreditApplication toDomain(CreditApplicationRequest request);

    @Mapping(target = "status", expression = "java(application.getStatus() != null ? application.getStatus().name() : null)")
    @Mapping(target = "monthlyPayment", expression = "java(application.calculateMonthlyPayment())")
    CreditApplicationResponse toResponse(CreditApplication application);
}
