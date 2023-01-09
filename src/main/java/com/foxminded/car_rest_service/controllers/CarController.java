package com.foxminded.car_rest_service.controllers;

import com.foxminded.car_rest_service.exceptions.response.ResultModel;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import java.util.List;

import static java.lang.String.format;

@Slf4j
@Validated
@RestController
@RequestMapping(Mappings.API_V1_CARS)
public class CarController implements CarOpenApi {

    @Autowired
    private CarService carService;

    @Override
    @GetMapping
    public ResponseEntity<ResultModel> getAllCars(Pageable pageable) {
        log.info("GetAllCars started");

        ResultModel resultModel = new ResultModel();
        List<CarDTO> cars = carService.getAllCars(pageable);

        if (cars.isEmpty()) {
            resultModel.setMassage("Cars not found");
            return new ResponseEntity<>(resultModel, HttpStatus.NOT_FOUND);
        }
        resultModel.setData(cars);

        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }

    @Override
    @GetMapping(Mappings.GET_CARS_BY_MANUFACTURER)
    public ResponseEntity<ResultModel> getAllCarsByManufacturer(@NotBlank @RequestParam String manufacturer, Pageable pageable) {
        log.info("GetAllCarsByManufacturer started with manufacturer: {}", manufacturer);

        ResultModel resultModel = new ResultModel();
        List<CarDTO> cars = carService.getAllCarsByManufacturer(manufacturer, pageable);

        if (cars.isEmpty()) {
            resultModel.setMassage(format("Cars with manufacturer(%s) not found", manufacturer));
            return new ResponseEntity<>(resultModel, HttpStatus.NOT_FOUND);
        }

        resultModel.setData(cars);

        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }

    @Override
    @GetMapping(Mappings.GET_CARS_BY_MANUFACTURER_AND_YEAR)
    public ResponseEntity<ResultModel> getAllCarsByManufacturerAndMinYear(@NotBlank @RequestParam String manufacturer,
                                                                          @RequestParam Integer year,
                                                                          Pageable pageable) {
        log.info("GetAllCarsByManufacturerAndMinYear started with manufacturer: {}, year: {}", manufacturer, year);

        ResultModel resultModel = new ResultModel();
        List<CarDTO> cars = carService.getAllCarsByManufacturerAndMinYear(manufacturer, year, pageable);
        if (cars.isEmpty()) {
            resultModel.setMassage(format("Cars with manufacturer(%s) and year(%s) not found", manufacturer, year));
            return new ResponseEntity<>(resultModel, HttpStatus.NOT_FOUND);
        }

        resultModel.setData(cars);
        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }

    @Override
    @PostMapping(Mappings.CREATE_CAR)
    public ResponseEntity<ResultModel> createCar(@NotBlank @PathVariable(name = "manufacturer") String manufacturer,
                                                 @NotBlank @PathVariable(name = "model") String model,
                                                 @PathVariable(name = "year") Integer year) {
        log.info("CreateCar started with manufacturer: {}, model: {}, year: {} ", manufacturer, model, year);

        ResultModel resultModel = new ResultModel();

        CarWithoutCategoriesDTO car = carService.createCar(manufacturer, model, year);

        if (car == null) {
            resultModel.setMassage(format("Car with manufacturer:%s, model:%s, year:%d, already exist",
                    manufacturer, model, year));

            return new ResponseEntity<>(resultModel, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        resultModel.setData(car);

        return new ResponseEntity<>(resultModel, HttpStatus.CREATED);
    }

    @Override
    @PutMapping
    public ResponseEntity<ResultModel> updateCar(@Valid @RequestBody CarWithoutCategoriesDTO carWithoutCategoriesDTO) {
        log.info("UpdateCar started");

        ResultModel resultModel = new ResultModel();

        CarWithoutCategoriesDTO car = carService.updateCar(carWithoutCategoriesDTO);

        if (car == null) {
            resultModel.setMassage(format("Car with id(%d) not found", carWithoutCategoriesDTO.getId()));
            return new ResponseEntity<>(resultModel, HttpStatus.NOT_FOUND);
        }

        resultModel.setData(car);

        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }


    @Override
    @DeleteMapping(Mappings.DELETE_CAR_BY_ID)
    public ResponseEntity<?> deleteCarById(@PathVariable(name = "id") Long id) {
        log.info("DeleteCarById started with id: {}", id);

        boolean isDeleted = carService.deleteCarById(id);

        if (!isDeleted) {
            ResultModel resultModel = new ResultModel();
            resultModel.setMassage(format("Car with id(%d) wasn't found", id));
            return new ResponseEntity<>(resultModel, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    @PutMapping(Mappings.ADD_CAR_TO_CATEGORY)
    public ResponseEntity<ResultModel> addCarToCategory(@PathVariable(name = "id") Long id,
                                                        @NotBlank @PathVariable(name = "name") String name) {
        log.info("AddCarToCategory started with id: {}, name: {}", id, name);

        ResultModel resultModel = new ResultModel();

        CarDTO car = carService.addCarToCategory(id, name);

        if (car == null) {
            resultModel.setMassage(format("Car with input id(%d) wasn't found", id));

            return new ResponseEntity<>(resultModel, HttpStatus.NOT_FOUND);
        }

        resultModel.setData(car);

        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }

    @Override
    @PutMapping(Mappings.DELETE_CAR_FROM_CATEGORY)
    public ResponseEntity<ResultModel> removeCarFromCategory(@PathVariable(name = "id") Long id,
                                                             @NotBlank @PathVariable(name = "name") String name) {
        log.info("RemoveCarFromCategory started with id: {}, name: {}", id, name);

        ResultModel resultModel = new ResultModel();

        CarDTO car = carService.removeCarFromCategory(id, name);

        if (car == null) {
            resultModel.setMassage(format("Car with input id(%d) wasn't found", id));

            return new ResponseEntity<>(resultModel, HttpStatus.NOT_FOUND);
        }

        resultModel.setData(car);

        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }
}
