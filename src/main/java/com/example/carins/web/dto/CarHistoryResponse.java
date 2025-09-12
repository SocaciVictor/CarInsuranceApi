package com.example.carins.web.dto;

import java.util.List;

public record CarHistoryResponse(
        long carId,
        List<CarEventDto> events
) {
}
