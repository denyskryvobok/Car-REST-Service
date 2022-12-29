package com.foxminded.car_rest_service.mapstruct.mapper;

import com.foxminded.car_rest_service.entities.Car;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarDTO;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarWithoutCategoriesDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarMapper extends MainMapper{

    CarDTO carToCarDTO(Car car);

    CarWithoutCategoriesDTO carToCarWithoutCategoriesDTO(Car car);
}
