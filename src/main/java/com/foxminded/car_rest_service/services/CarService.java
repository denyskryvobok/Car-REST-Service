package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.mapstruct.dto.car.CarDTO;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarWithoutCategoriesDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CarService {
    List<CarDTO> getAllCars(Pageable pageable);

    CarWithoutCategoriesDTO createCar(String manufacturer, String model, Integer year);

    boolean deleteCarById(Long id);

    CarDTO addCarToCategory(Long id, String name);

    CarDTO removeCarFromCategory(Long id, String name);

    List<CarDTO> getAllCarsByManufacturer(String manufacturer, Pageable pageable);

    List<CarDTO> getAllCarsByManufacturerAndMinYear(String manufacturer, Integer year, Pageable pageable);

    CarWithoutCategoriesDTO updateCar(CarWithoutCategoriesDTO carWithoutCategoriesDTO);
}
