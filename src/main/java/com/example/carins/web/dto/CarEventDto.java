package com.example.carins.web.dto;

import com.example.carins.web.enums.CarEventType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CarEventDto(
        CarEventType type,
        LocalDate date,
        String description,
        BigDecimal amount
) {
}
