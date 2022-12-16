package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.entities.Car;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CarService {
    List<Car> getAllCars(Pageable pageable);

    Car createCar(String manufacturer, String model, Integer year);

    void deleteCarById(Long id);

    Car addCarToCategory(Long id, String name);

    Car removeCarFromCategory(Long id, String name);

    List<Car> getAllCarsByManufacturer(String manufacturer, Pageable pageable);

    List<Car> getAllCarsByManufacturerAndMinYear(String manufacturer, Integer year, Pageable pageable);
}
