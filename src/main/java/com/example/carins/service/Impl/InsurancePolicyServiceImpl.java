package com.example.carins.service.Impl;

import com.example.carins.Exception.CarNotFoundException;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.service.InsurancePolicyService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
public class InsurancePolicyServiceImpl implements InsurancePolicyService {
    private final CarRepository carRepository;
    private final InsurancePolicyRepository insurancePolicyRepository;

    public InsurancePolicyServiceImpl(CarRepository carRepository, InsurancePolicyRepository insurancePolicyRepository) {
        this.carRepository = carRepository;
        this.insurancePolicyRepository = insurancePolicyRepository;
    }

    @Override
    public boolean isInsuranceValid(Long carId, LocalDate date) {

        if (!carRepository.existsById(carId)){
            throw new CarNotFoundException(carId);
        }
        return insurancePolicyRepository.existsActiveOnDate(carId, date);
    }
}
