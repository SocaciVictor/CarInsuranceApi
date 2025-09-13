package com.example.carins.service;

import com.example.carins.model.Car;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.service.CarValidatorService;
import com.example.carins.service.InsuranceValidatorService;
import com.example.carins.service.Impl.InsurancePolicyServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InsurancePolicyServiceImplTest {

    @Mock CarRepository carRepo;
    @Mock InsurancePolicyRepository policyRepo;
    @Mock CarValidatorService carValidator;
    @Mock InsuranceValidatorService validator;

    @InjectMocks
    InsurancePolicyServiceImpl service;

    @Test
    void validPolicy_returnsTrue() {
        when(policyRepo.existsActiveOnDate(1L, LocalDate.of(2025,6,1))).thenReturn(true);
        when(carValidator.getExistingCarOrThrow(1L)).thenReturn(new Car());
        assertThat(service.isInsuranceValid(1L, LocalDate.of(2025,6,1))).isTrue();
    }
}

