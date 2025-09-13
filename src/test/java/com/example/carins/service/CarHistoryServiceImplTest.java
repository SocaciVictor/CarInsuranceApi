package com.example.carins.service;

import com.example.carins.Exception.CarNotFoundException;
import com.example.carins.model.Car;
import com.example.carins.model.InsuranceClaim;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.model.Owner;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsuranceClaimRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.service.Impl.CarHistoryServiceImpl;
import com.example.carins.web.dto.CarEventDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CarHistoryServiceImplTest {

    @Mock CarRepository carRepo;
    @Mock InsurancePolicyRepository policyRepo;
    @Mock InsuranceClaimRepository claimRepo;

    @InjectMocks
    CarHistoryServiceImpl service;

    @Test
    void getHistory_returnsEventsSorted_andCarInfo() {
        var owner = new Owner("Ana Pop", "ana@example.com");
        owner.setId(99L);

        var car = new Car();
        car.setId(1L);
        car.setVin("VIN12345");
        car.setMake("Dacia");
        car.setModel("Logan");
        car.setYearOfManufacture(2018);
        car.setOwner(owner);

        when(carRepo.findById(1L)).thenReturn(Optional.of(car));

        var policy = new InsurancePolicy();
        policy.setStartDate(LocalDate.of(2024,1,1));
        policy.setEndDate(LocalDate.of(2024,12,31));
        policy.setProvider("Allianz");

        var claim = new InsuranceClaim();
        claim.setClaimDate(LocalDate.of(2024,6,1));
        claim.setDescription("Scratch");
        claim.setAmount(BigDecimal.valueOf(100));

        when(policyRepo.findByCarId(1L)).thenReturn(List.of(policy));
        when(claimRepo.findByCarIdOrderByClaimDateAsc(1L)).thenReturn(List.of(claim));

        var resp = service.getHistory(1L);

        assertThat(resp.carId()).isEqualTo(1L);
        assertThat(resp.vin()).isEqualTo("VIN12345");
        assertThat(resp.ownerName()).isEqualTo("Ana Pop");
        assertThat(resp.events()).hasSize(3);
        assertThat(resp.events())
                .isSortedAccordingTo(Comparator.comparing(CarEventDto::date));
    }


    @Test
    void getHistory_carNotFound_throws() {
        when(carRepo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class, () -> service.getHistory(2L));
    }

}

