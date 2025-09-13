package com.example.carins.service;

import com.example.carins.Exception.CarNotFoundException;
import com.example.carins.model.InsuranceClaim;
import com.example.carins.model.InsurancePolicy;
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
    void getHistory_returnsEventsSorted() {
        when(carRepo.existsById(1L)).thenReturn(true);

        var p = new InsurancePolicy();
        p.setStartDate(LocalDate.of(2024,1,1));
        p.setEndDate(LocalDate.of(2024,12,31));
        p.setProvider("Allianz");

        var c = new InsuranceClaim();
        c.setClaimDate(LocalDate.of(2024,6,1));
        c.setDescription("Scratch");
        c.setAmount(BigDecimal.valueOf(100));

        when(policyRepo.findByCarId(1L)).thenReturn(List.of(p));
        when(claimRepo.findByCarIdOrderByClaimDateAsc(1L)).thenReturn(List.of(c));

        var resp = service.getHistory(1L);

        assertThat(resp.events()).hasSize(3);
        assertThat(resp.events()).isSortedAccordingTo(Comparator.comparing(CarEventDto::date));
    }

    @Test
    void getHistory_carNotFound_throws() {
        when(carRepo.existsById(2L)).thenReturn(false);
        assertThrows(CarNotFoundException.class, () -> service.getHistory(2L));
    }
}

