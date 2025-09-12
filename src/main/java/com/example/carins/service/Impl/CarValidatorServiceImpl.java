package com.example.carins.service.Impl;

import com.example.carins.Exception.CarNotFoundException;
import com.example.carins.model.Car;
import com.example.carins.repo.CarRepository;
import com.example.carins.service.CarValidatorService;
import org.springframework.stereotype.Service;

@Service
public class CarValidatorServiceImpl implements CarValidatorService {
    private final CarRepository carRepository;

    public CarValidatorServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car getExistingCarOrThrow(Long carId) {
        return carRepository.findById(carId).orElseThrow(() -> new CarNotFoundException(carId));
    }

    public void ensureVinFormat(String vin) {
        if (vin == null || vin.length() < 5 || vin.length() > 32) {
            throw new IllegalArgumentException("VIN must be between 5 and 32 characters");
        }
    }
}
