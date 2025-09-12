package com.example.carins.web;

import com.example.carins.Exception.InvalidDateException;
import com.example.carins.mapper.CarMapper;
import com.example.carins.model.Car;
import com.example.carins.service.Impl.CarServiceImpl;
import com.example.carins.service.InsurancePolicyService;
import com.example.carins.web.dto.CarDto;
import com.example.carins.web.dto.InsuranceValidityResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CarController {

    private final InsurancePolicyService insurancePolicyService;
    private final CarServiceImpl service;
    private final CarMapper carMapper;

    public CarController(CarServiceImpl service,  InsurancePolicyService insurancePolicyService,  CarMapper carMapper) {
        this.service = service;
        this.insurancePolicyService = insurancePolicyService;
        this.carMapper = carMapper;
    }

    @GetMapping("/cars")
    public List<CarDto> getCars() {
        return service.listCars().stream().map(carMapper::toDto).toList();
    }

    @GetMapping("/cars/{carId}/insurance-valid")
    public ResponseEntity<InsuranceValidityResponse> isInsuranceValid(
            @PathVariable Long carId,
            @RequestParam String date)
    {
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(date);
        } catch (Exception e) {
            throw new InvalidDateException("Date must be in format YYYY-MM-DD");
        }
        if (parsedDate.isBefore(LocalDate.of(1900,1,1)) ||
                parsedDate.isAfter(LocalDate.of(2100,12,31))) {
            throw new InvalidDateException("Date out of supported range");
        }
        boolean valid = insurancePolicyService.isInsuranceValid(carId, parsedDate);
        return ResponseEntity.ok(new InsuranceValidityResponse(carId, parsedDate.toString(), valid));
    }
}
