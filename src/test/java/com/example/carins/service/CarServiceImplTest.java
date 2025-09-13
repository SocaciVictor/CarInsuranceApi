package com.example.carins.service;

import com.example.carins.mapper.CarMapper;
import com.example.carins.model.Car;
import com.example.carins.model.Owner;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.repo.OwnerRepository;
import com.example.carins.service.Impl.CarServiceImpl;
import com.example.carins.web.dto.CarDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock CarRepository carRepo;
    @Mock InsurancePolicyRepository polRepo;
    @Mock CarMapper mapper;
    @Mock OwnerRepository ownerRepo;

    @InjectMocks
    CarServiceImpl service;

    @Test
    void createFromDto_withOwner() {
        var dto = new CarDto(1L,"VIN123","Dacia","Logan",2020,1L,"Ana","ana@example.com");
        var owner = new Owner("Ana","ana@example.com");
        when(ownerRepo.findById(1L)).thenReturn(Optional.of(owner));

        Car carEntity = new Car();
        when(mapper.toEntity(dto)).thenReturn(carEntity);
        when(carRepo.save(any())).thenReturn(carEntity);

        var result = service.createFromDto(dto);
        assertThat(result).isNotNull();
        verify(ownerRepo).findById(1L);
        verify(carRepo).save(carEntity);
    }
}

