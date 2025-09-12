package com.example.carins.Exception;

public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException(Long carId) {
        super("Car with id " + carId + " not found");
    }
}
