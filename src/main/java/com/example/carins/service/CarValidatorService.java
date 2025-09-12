package com.example.carins.service;

import com.example.carins.model.Car;

public interface CarValidatorService {
    Car getExistingCarOrThrow(Long carId);
    void ensureVinFormat(String vin);
}
