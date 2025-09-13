package com.example.carins.service;

import com.example.carins.model.Car;
import com.example.carins.model.InsuranceClaim;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsuranceClaimRepository;
import com.example.carins.service.CarValidatorService;
import com.example.carins.service.Impl.InsuranceClaimServiceImpl;
import com.example.carins.web.dto.InsuranceClaimRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InsuranceClaimServiceImplTest {

    @Mock InsuranceClaimRepository claimRepo;
    @Mock CarRepository carRepo;
    @Mock CarValidatorService carValidator;
    @InjectMocks InsuranceClaimServiceImpl service;

    @Test
    void create_savesClaim() {
        var car = new Car(); car.setId(1L);
        when(carValidator.getExistingCarOrThrow(1L)).thenReturn(car);

        var req = new InsuranceClaimRequest(LocalDate.now(), "desc", BigDecimal.TEN);
        var saved = new InsuranceClaim(car, req.claimDate(), req.description(), req.amount());
        when(claimRepo.save(any())).thenReturn(saved);

        var result = service.create(1L, req);
        assertThat(result.getDescription()).isEqualTo("desc");
        verify(claimRepo).save(any());
    }
}

