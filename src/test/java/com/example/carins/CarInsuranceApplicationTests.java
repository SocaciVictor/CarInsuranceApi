package com.example.carins;

import com.example.carins.service.Impl.CarServiceImpl;
import com.example.carins.service.InsurancePolicyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CarInsuranceApplicationTests {

    @Autowired
    CarServiceImpl service;
    @Autowired
    InsurancePolicyService policyService;

    @Test
    void insuranceValidityBasic() {
        assertTrue(policyService.isInsuranceValid(1L, LocalDate.parse("2024-06-01")));
        assertFalse(policyService.isInsuranceValid(1L, LocalDate.parse("2025-06-01")));
        assertFalse(policyService.isInsuranceValid(2L, LocalDate.parse("2025-02-01")));
    }
}
