package com.example.carins.service;

import com.example.carins.model.Car;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.service.Impl.PolicyExpirationLoggerImpl;
import org.junit.jupiter.api.Test;

import java.time.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class PolicyExpirationLoggerUnitTest {

    @Test
    void marksPoliciesAndLogs() {
        var repo = mock(InsurancePolicyRepository.class);
        Clock fixedClock = Clock.fixed(Instant.parse("2025-01-01T00:10:00Z"), ZoneOffset.UTC);

        var service = new PolicyExpirationLoggerImpl(repo, fixedClock);

        Car car = new Car();
        car.setId(1L);
        InsurancePolicy policy = new InsurancePolicy(car, "Allianz",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31));
        policy.setId(10L);

        when(repo.findExpiredUnlogged(LocalDate.of(2025, 1, 1))).thenReturn(List.of(policy));

        service.logAndMarkExpiredPolicies();

        assertTrue(policy.isExpiredLogged());
        verify(repo).findExpiredUnlogged(LocalDate.of(2025, 1, 1));
    }
}
