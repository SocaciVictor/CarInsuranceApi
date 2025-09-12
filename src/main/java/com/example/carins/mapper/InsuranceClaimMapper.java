package com.example.carins.mapper;

import com.example.carins.model.InsuranceClaim;
import com.example.carins.web.dto.InsuranceClaimResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InsuranceClaimMapper {
    @Mapping(source = "car.id", target = "carId")
    InsuranceClaimResponse toResponse(InsuranceClaim claim);
}
