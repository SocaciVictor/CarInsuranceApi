package com.example.carins.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CarEventDto(
        String type,
        LocalDate date,
        String description,
        BigDecimal amount
) {
}
