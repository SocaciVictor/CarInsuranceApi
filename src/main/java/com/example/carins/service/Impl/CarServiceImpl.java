package com.example.carins.service.Impl;

import com.example.carins.mapper.CarMapper;
import com.example.carins.model.Car;
import com.example.carins.model.Owner;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.repo.OwnerRepository;
import com.example.carins.service.CarService;
import com.example.carins.web.dto.CarDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final InsurancePolicyRepository policyRepository;
    private final CarMapper carMapper;
    private final OwnerRepository ownerRepository;

    public CarServiceImpl(CarRepository carRepository, InsurancePolicyRepository policyRepository,  CarMapper carMapper, OwnerRepository ownerRepository) {
        this.carRepository = carRepository;
        this.policyRepository = policyRepository;
        this.carMapper = carMapper;
        this.ownerRepository = ownerRepository;
    }
    public Car createFromDto(CarDto dto) {
        Car car = carMapper.toEntity(dto);

        if (dto.ownerId() != null) {
            Owner owner = ownerRepository.findById(dto.ownerId())
                    .orElseThrow(() -> new EntityNotFoundException("Owner not found"));
            car.setOwner(owner);
        }

        return carRepository.save(car);
    }
    public List<Car> listCars() {
        return carRepository.findAll();
    }

    public boolean isInsuranceValid(Long carId, LocalDate date) {
        if (carId == null || date == null) return false;
        // TODO: optionally throw NotFound if car does not exist
        return policyRepository.existsActiveOnDate(carId, date);
    }
}
