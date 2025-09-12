package com.example.carins.web;

import com.example.carins.Exception.InvalidDateException;
import com.example.carins.mapper.CarMapper;
import com.example.carins.mapper.InsuranceClaimMapper;
import com.example.carins.service.CarService;
import com.example.carins.service.DateParserService;
import com.example.carins.service.InsuranceClaimService;
import com.example.carins.service.InsurancePolicyService;
import com.example.carins.web.dto.CarDto;
import com.example.carins.web.dto.InsuranceClaimRequest;
import com.example.carins.web.dto.InsuranceClaimResponse;
import com.example.carins.web.dto.InsuranceValidityResponse;
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

    public CarController(CarService carService,
                         InsurancePolicyService insurancePolicyService,
                         InsuranceClaimService insuranceClaimService,
                         CarMapper carMapper,
                         InsuranceClaimMapper claimMapper,
                         DateParserService dateParser,
                         LocationBuilder locationBuilder) {
        this.carService = carService;
        this.insurancePolicyService = insurancePolicyService;
        this.carMapper = carMapper;
        this.insuranceClaimService = insuranceClaimService;
        this.insuranceClaimMapper = claimMapper;
        this.dateParserService = dateParser;
        this.locationBuilder = locationBuilder;
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
}
