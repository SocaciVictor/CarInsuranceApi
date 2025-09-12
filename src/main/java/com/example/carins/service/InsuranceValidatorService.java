package com.example.carins.service;

import java.time.LocalDate;

public interface InsuranceValidatorService {
    void ensurePolicyDates(LocalDate startDate, LocalDate endDate);
    void ensureSupportedDate(LocalDate date);
}
