package com.example.carins.web.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InsuranceClaimRequest(
        @NotNull(message = "Claim date is required")
        @PastOrPresent(message = "Claim date cannot be in the future")
        LocalDate claimDate,

        @NotBlank(message = "Description is required")
        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
        BigDecimal amount
) {
}
