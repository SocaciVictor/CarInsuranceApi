package com.example.carins.service.Impl;

import com.example.carins.Exception.CarNotFoundException;
import com.example.carins.model.Car;
import com.example.carins.model.InsuranceClaim;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsuranceClaimRepository;
import com.example.carins.service.CarValidatorService;
import com.example.carins.service.InsuranceClaimService;
import com.example.carins.web.dto.InsuranceClaimRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsuranceClaimServiceImpl implements InsuranceClaimService {
    private final InsuranceClaimRepository claimRepo;
    private final CarRepository carRepo;
    private final CarValidatorService carValidator;

    public InsuranceClaimServiceImpl(InsuranceClaimRepository claimRepo,
                                     CarRepository carRepo,
                                     CarValidatorService carValidator) {
        this.claimRepo = claimRepo;
        this.carRepo = carRepo;
        this.carValidator = carValidator;
    }

    @Override
    public InsuranceClaim create(Long carId, InsuranceClaimRequest dto) {
        var car = carValidator.getExistingCarOrThrow(carId);
        var claim = new InsuranceClaim(car, dto.claimDate(), dto.description(), dto.amount());
        return claimRepo.save(claim);
    }

    @Override
    public List<InsuranceClaim> findByCarId(Long carId) {
        carValidator.getExistingCarOrThrow(carId);
        return claimRepo.findByCarIdOrderByClaimDateAsc(carId);
    }

}
