package com.foxminded.car_rest_service.controllers;

import com.foxminded.car_rest_service.mapstruct.dto.car.CarDTO;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarWithoutCategoriesDTO;
import com.foxminded.car_rest_service.services.CarService;
import com.foxminded.car_rest_service.utils.Mappings;
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

@Slf4j
@Validated
@RestController
@RequestMapping(Mappings.API_V1_CARS)
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping
    public ResponseEntity<List<CarDTO>> getAllCars(Pageable pageable) {
        log.info("GetAllCars started");

        return new ResponseEntity<>(carService.getAllCars(pageable), HttpStatus.OK);
    }

    @GetMapping(Mappings.GET_CARS_BY_MANUFACTURER)
    public ResponseEntity<List<CarDTO>> getAllCarsByManufacturer(@NotBlank @RequestParam String manufacturer, Pageable pageable) {
        log.info("GetAllCarsByManufacturer started with manufacturer: {}", manufacturer);

        return new ResponseEntity<>(carService.getAllCarsByManufacturer(manufacturer, pageable), HttpStatus.OK);
    }

    @GetMapping(Mappings.GET_CARS_BY_MANUFACTURER_AND_YEAR)
    public ResponseEntity<List<CarDTO>> getAllCarsByManufacturerAndMinYear(@NotBlank @RequestParam String manufacturer,
                                                           @RequestParam Integer year,
                                                           Pageable pageable) {
        log.info("GetAllCarsByManufacturerAndMinYear started with manufacturer: {}, year: {}", manufacturer, year);

        return new ResponseEntity<>(carService.getAllCarsByManufacturerAndMinYear(manufacturer, year, pageable), HttpStatus.OK);
    }

    @PostMapping(Mappings.CREATE_CAR)
    public ResponseEntity<CarWithoutCategoriesDTO> createCar(@NotBlank @PathVariable(name = "manufacturer") String manufacturer,
                                                             @NotBlank @PathVariable(name = "model") String model,
                                                             @PathVariable(name = "year") Integer year) {
        log.info("CreateCar started with manufacturer: {}, model: {}, year: {} ", manufacturer, model, year);

        return new ResponseEntity<>(carService.createCar(manufacturer, model, year), HttpStatus.CREATED);
    }

    @DeleteMapping(Mappings.DELETE_CAR_BY_ID)
    public ResponseEntity<?> deleteCarById(@PathVariable(name = "id") Long id) {
        log.info("DeleteCarById started with id: {}", id);

        carService.deleteCarById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(Mappings.ADD_CAR_TO_CATEGORY)
    public ResponseEntity<CarDTO> addCarToCategory(@PathVariable(name = "id") Long id,
                                                   @NotBlank @PathVariable(name = "name") String name) {
        log.info("AddCarToCategory started with id: {}, name: {}", id, name);

        return new ResponseEntity<>(carService.addCarToCategory(id, name), HttpStatus.OK);
    }

    @PutMapping(Mappings.DELETE_CAR_FROM_CATEGORY)
    public ResponseEntity<CarDTO> removeCarFromCategory(@PathVariable(name = "id") Long id,
                                                        @NotBlank @PathVariable(name = "name") String name) {
        log.info("RemoveCarFromCategory started with id: {}, name: {}", id, name);

        return new ResponseEntity<>(carService.removeCarFromCategory(id, name), HttpStatus.OK);
    }
}
