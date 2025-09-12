package com.example.carins.service;

import com.example.carins.model.InsuranceClaim;
import com.example.carins.web.dto.InsuranceClaimRequest;

import java.util.List;

public interface InsuranceClaimService {
    InsuranceClaim create(Long carId, InsuranceClaimRequest dto);
    List<InsuranceClaim> findByCarId(Long carId);
}
