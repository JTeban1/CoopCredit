package com.riwi.coopcredit.application.mapper;

import com.riwi.coopcredit.application.dto.request.AffiliateRequest;
import com.riwi.coopcredit.application.dto.response.AffiliateResponse;
import com.riwi.coopcredit.domain.model.Affiliate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AffiliateMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "affiliationDate", ignore = true)
    @Mapping(target = "active", ignore = true)
    Affiliate toDomain(AffiliateRequest request);

    AffiliateResponse toResponse(Affiliate affiliate);
}
