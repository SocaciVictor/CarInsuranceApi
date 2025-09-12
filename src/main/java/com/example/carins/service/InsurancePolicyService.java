package com.example.carins.service;

import java.time.LocalDate;

public interface InsurancePolicyService {
    boolean isInsuranceValid(Long carId, LocalDate date);
}
