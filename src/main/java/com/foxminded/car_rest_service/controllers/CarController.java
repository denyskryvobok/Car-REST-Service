package com.foxminded.car_rest_service.controllers;

import com.foxminded.car_rest_service.entities.Car;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarDTO;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarWithoutCategoriesDTO;
import com.foxminded.car_rest_service.mapstruct.mapper.CarMapper;
import com.foxminded.car_rest_service.services.CarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private CarMapper mapper;

    @GetMapping
    public List<CarDTO> getAllCars(Pageable pageable) {
        log.info("GetAllCars started");

        return carService.getAllCars(pageable).stream()
                .map(car -> mapper.carToCarDTO(car))
                .collect(toList());
    }

    @GetMapping("manufacturer")
    public List<CarDTO> getAllCarsByManufacturer(@NotBlank @RequestParam String manufacturer, Pageable pageable) {
        log.info("GetAllCarsByManufacturer started with manufacturer: {}", manufacturer);

        return carService.getAllCarsByManufacturer(manufacturer, pageable).stream()
                .map(car -> mapper.carToCarDTO(car))
                .collect(toList());
    }

    @GetMapping("/manufacturer/year")
    public List<CarDTO> getAllCarsByManufacturerAndMinYear(@NotBlank @RequestParam String manufacturer,
                                                           @RequestParam Integer year,
                                                           Pageable pageable) {
        log.info("GetAllCarsByManufacturerAndMinYear started with manufacturer: {}, year: {}", manufacturer, year);

        return carService.getAllCarsByManufacturerAndMinYear(manufacturer, year, pageable).stream()
                .map(car -> mapper.carToCarDTO(car))
                .collect(toList());
    }

    @PostMapping("manufacturer/{manufacturer}/model/{model}/year/{year}")
    public ResponseEntity<CarWithoutCategoriesDTO> createCar(@NotBlank @PathVariable(name = "manufacturer") String manufacturer,
                                                             @NotBlank @PathVariable(name = "model") String model,
                                                             @PathVariable(name = "year") Integer year) {
        log.info("CreateCar started with manufacturer: {}, model: {}, year: {} ", manufacturer, model, year);

        Car car = carService.createCar(manufacturer, model, year);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.carToCarWithoutCategoriesDTO(car));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCarById(@PathVariable(name = "id") Long id) {
        log.info("DeleteCarById started with id: {}", id);

        carService.deleteCarById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("add/{id}/category/{name}")
    public ResponseEntity<CarDTO> addCarToCategory(@PathVariable(name = "id") Long id,
                                                   @NotBlank @PathVariable(name = "name") String name) {
        log.info("AddCarToCategory started with id: {}, name: {}", id, name);

        return ResponseEntity.ok().body(mapper.carToCarDTO(carService.addCarToCategory(id, name)));
    }

    @PutMapping("remove/{id}/category/{name}")
    public ResponseEntity<CarDTO> removeCarFromCategory(@PathVariable(name = "id") Long id,
                                                        @NotBlank @PathVariable(name = "name") String name) {
        log.info("RemoveCarFromCategory started with id: {}, name: {}", id, name);

        Car car = carService.removeCarFromCategory(id, name);

        return ResponseEntity.ok().body(mapper.carToCarDTO(car));
    }

}
