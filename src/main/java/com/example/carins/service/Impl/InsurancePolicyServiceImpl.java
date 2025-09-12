package com.example.carins.service.Impl;

import com.example.carins.Exception.CarNotFoundException;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.service.CarValidatorService;
import com.example.carins.service.InsurancePolicyService;
import com.example.carins.service.InsuranceValidatorService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class InsurancePolicyServiceImpl implements InsurancePolicyService {
    private final CarRepository carRepository;
    private final InsurancePolicyRepository insurancePolicyRepository;
    private final CarValidatorService carValidator;
    private final InsuranceValidatorService insuranceValidator;

    public InsurancePolicyServiceImpl(CarRepository carRepository,
                                      InsurancePolicyRepository insurancePolicyRepository,
                                      CarValidatorService carValidator,
                                      InsuranceValidatorService insuranceValidator) {
        this.carRepository = carRepository;
        this.insurancePolicyRepository = insurancePolicyRepository;
        this.carValidator = carValidator;
        this.insuranceValidator = insuranceValidator;
    }

    @Override
    public boolean isInsuranceValid(Long carId, LocalDate date) {
        carValidator.getExistingCarOrThrow(carId);
        insuranceValidator.ensureSupportedDate(date);
        return insurancePolicyRepository.existsActiveOnDate(carId, date);
    }
}
