package com.example.carins.web.dto;

import java.util.List;

public record CarHistoryResponse(
        Long carId,
        String vin,
        String make,
        String model,
        int yearOfManufacture,
        Long ownerId,
        String ownerName,
        List<CarEventDto> events
) {
}
