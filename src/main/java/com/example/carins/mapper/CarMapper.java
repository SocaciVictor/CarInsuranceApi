package com.example.carins.mapper;

import com.example.carins.model.Car;
import com.example.carins.web.dto.CarDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mapping(source = "yearOfManufacture", target = "year")
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.name", target = "ownerName")
    @Mapping(source = "owner.email", target = "ownerEmail")
    CarDto toDto(Car car);

    @Mapping(source = "year", target = "yearOfManufacture")
    @Mapping(target = "owner", ignore = true)
    Car toEntity(CarDto dto);
}
