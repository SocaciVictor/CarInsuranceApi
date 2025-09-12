package com.example.carins.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "insurance_claim")
public class InsuranceClaim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Car car;

    @NotNull
    private LocalDate claimDate;

    @NotBlank
    @Size(max = 500)
    private String description;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal amount;

    public InsuranceClaim() {}
    public InsuranceClaim(Car car, LocalDate date, String description, BigDecimal amount) {
        this.car = car;
        this.claimDate = date;
        this.description = description;
        this.amount = amount;
    }
    public Long getId() { return id; }
    public Car getCar() { return car; }
    public LocalDate getClaimDate() { return claimDate; }
    public String getDescription() { return description; }
    public BigDecimal getAmount() { return amount; }
}
