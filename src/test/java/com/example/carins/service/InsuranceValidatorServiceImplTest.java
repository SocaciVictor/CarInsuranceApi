package com.example.carins.service;

import com.example.carins.service.Impl.InsuranceValidatorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class InsuranceValidatorServiceImplTest {

    InsuranceValidatorServiceImpl service = new InsuranceValidatorServiceImpl();

    @Test
    void ensurePolicyDates_valid() {
        service.ensurePolicyDates(LocalDate.of(2024,1,1), LocalDate.of(2024,12,31));
    }

    @Test
    void ensurePolicyDates_endBeforeStart() {
        assertThrows(IllegalArgumentException.class,
                () -> service.ensurePolicyDates(LocalDate.of(2024,12,31), LocalDate.of(2024,1,1)));
    }

    @Test
    void ensureSupportedDate_valid() {
        service.ensureSupportedDate(LocalDate.of(2025,1,1));
    }
}

