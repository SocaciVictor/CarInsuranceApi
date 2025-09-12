package com.example.carins.service;

import com.example.carins.model.InsuranceClaim;
import com.example.carins.web.dto.InsuranceClaimRequest;

public interface InsuranceClaimService {
    InsuranceClaim create(Long carId, InsuranceClaimRequest dto);

}
