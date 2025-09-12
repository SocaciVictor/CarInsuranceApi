package com.example.carins.web.dto;

import java.util.List;

public record CarHistoryResponse(
        Long carId,
        List<CarEventDto> events
) {
}
