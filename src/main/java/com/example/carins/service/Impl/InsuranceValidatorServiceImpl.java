package com.example.carins.service.Impl;

import com.example.carins.service.InsuranceValidatorService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class InsuranceValidatorServiceImpl implements InsuranceValidatorService {

    public void ensurePolicyDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) throw new IllegalArgumentException("startDate is required");
        if (endDate == null) throw new IllegalArgumentException("endDate is required");
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate must be on or after startDate");
        }
    }

    public void ensureSupportedDate(LocalDate date) {
        if (date.isBefore(LocalDate.of(1900,1,1)) || date.isAfter(LocalDate.of(2100,12,31))) {
            throw new IllegalArgumentException("Date out of supported range");
        }
    }
}

