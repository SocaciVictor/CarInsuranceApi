package com.example.carins.web;

import com.example.carins.Exception.InvalidDateException;
import com.example.carins.mapper.CarMapper;
import com.example.carins.mapper.InsuranceClaimMapper;
import com.example.carins.service.*;
import com.example.carins.web.dto.*;
import com.example.carins.web.util.LocationBuilder;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CarController {

    private final InsurancePolicyService insurancePolicyService;
    private final InsuranceClaimService  insuranceClaimService;
    private final CarService carService;
    private final CarMapper carMapper;
    private final InsuranceClaimMapper insuranceClaimMapper;
    private final DateParserService dateParserService;
    private final LocationBuilder locationBuilder;
    private final CarHistoryService carHistoryService;

    public CarController(CarService carService,
                         InsurancePolicyService insurancePolicyService,
                         InsuranceClaimService insuranceClaimService,
                         CarMapper carMapper,
                         InsuranceClaimMapper claimMapper,
                         DateParserService dateParser,
                         LocationBuilder locationBuilder,
                         CarHistoryService carHistoryService) {
        this.carService = carService;
        this.insurancePolicyService = insurancePolicyService;
        this.carMapper = carMapper;
        this.insuranceClaimService = insuranceClaimService;
        this.insuranceClaimMapper = claimMapper;
        this.dateParserService = dateParser;
        this.locationBuilder = locationBuilder;
        this.carHistoryService = carHistoryService;
    }

    @GetMapping("/cars")
    public List<CarDto> getCars() {
        return carService.listCars().stream().map(carMapper::toDto).toList();
    }

    @GetMapping("/cars/{carId}/insurance-valid")
    public ResponseEntity<InsuranceValidityResponse> isInsuranceValid(
            @PathVariable Long carId,
            @RequestParam String date)
    {
        LocalDate parsedDate = dateParserService.parseAndValidate(date);
        boolean valid = insurancePolicyService.isInsuranceValid(carId, parsedDate);
        return ResponseEntity.ok(new InsuranceValidityResponse(carId, parsedDate.toString(), valid));
    }

    @PostMapping("/cars/{carId}/claims")
    public ResponseEntity<InsuranceClaimResponse> createClaim(
            @PathVariable Long carId,
            @Valid @RequestBody InsuranceClaimRequest request) {

        var claim = insuranceClaimService.create(carId, request);
        var body = insuranceClaimMapper.toResponse(claim);
        URI location = locationBuilder.forClaim(claim.getId());
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/cars/{carId}/claims")
    public ResponseEntity<List<InsuranceClaimResponse>> getClaims(@PathVariable Long carId) {
        var claims = insuranceClaimService.findByCarId(carId)
                .stream()
                .map(insuranceClaimMapper::toResponse)
                .toList();
        return ResponseEntity.ok(claims);
    }

    @GetMapping("/cars/{carId}/history")
    public ResponseEntity<CarHistoryResponse> getCarHistory(@PathVariable Long carId) {
        var resp = carHistoryService.getHistory(carId);
        return ResponseEntity.ok(resp);
    }

}
