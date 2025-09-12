package com.example.carins.service.Impl;

import com.example.carins.Exception.CarNotFoundException;
import com.example.carins.model.InsuranceClaim;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsuranceClaimRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.service.CarHistoryService;
import com.example.carins.web.dto.*;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CarHistoryServiceImpl implements CarHistoryService {

    private final CarRepository carRepo;
    private final InsurancePolicyRepository policyRepo;
    private final InsuranceClaimRepository claimRepo;

    public CarHistoryServiceImpl(CarRepository carRepo,
                                 InsurancePolicyRepository policyRepo,
                                 InsuranceClaimRepository claimRepo) {
        this.carRepo = carRepo;
        this.policyRepo = policyRepo;
        this.claimRepo = claimRepo;
    }

    @Override
    public CarHistoryResponse getHistory(Long carId) {
        if (!carRepo.existsById(carId)) {
            throw new CarNotFoundException(carId);
        }

        List<CarEventDto> events = new ArrayList<>();

        for (InsurancePolicy p : policyRepo.findByCarId(carId)) {
            events.add(new CarEventDto(
                    "POLICY_START",
                    p.getStartDate(),
                    "Policy started (" + p.getProvider() + ")",
                    null
            ));
            events.add(new CarEventDto(
                    "POLICY_END",
                    p.getEndDate(),
                    "Policy ended (" + p.getProvider() + ")",
                    null
            ));
        }

        for (InsuranceClaim c : claimRepo.findByCarIdOrderByClaimDateAsc(carId)) {
            events.add(new CarEventDto(
                    "CLAIM",
                    c.getClaimDate(),
                    c.getDescription(),
                    c.getAmount()
            ));
        }

        events.sort(Comparator.comparing(CarEventDto::date));

        return new CarHistoryResponse(carId, events);
    }
}
