package com.example.carins.service;

import com.example.carins.Exception.CarNotFoundException;
import com.example.carins.model.Car;
import com.example.carins.repo.CarRepository;
import com.example.carins.service.Impl.CarValidatorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarValidatorServiceImplTest {

    @Mock CarRepository repo;
    @InjectMocks CarValidatorServiceImpl service;

    @Test
    void getExistingCar_returnsCar() {
        Car c = new Car(); c.setId(5L);
        when(repo.findById(5L)).thenReturn(Optional.of(c));
        assertThat(service.getExistingCarOrThrow(5L)).isEqualTo(c);
    }

    @Test
    void getExistingCar_throwsIfNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class, () -> service.getExistingCarOrThrow(99L));
    }

    @Test
    void ensureVinFormat_acceptsValidVin() {
        service.ensureVinFormat("ABCDE12345");
    }

    @Test
    void ensureVinFormat_rejectsShortVin() {
        assertThrows(IllegalArgumentException.class, () -> service.ensureVinFormat("123"));
    }
}
