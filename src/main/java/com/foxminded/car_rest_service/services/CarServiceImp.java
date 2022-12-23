package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.dao.CarDAO;
import com.foxminded.car_rest_service.dao.CategoryDAO;
import com.foxminded.car_rest_service.dao.ManufacturerDAO;
import com.foxminded.car_rest_service.dao.ModelDAO;
import com.foxminded.car_rest_service.entities.Car;
import com.foxminded.car_rest_service.entities.CarCategoryInfo;
import com.foxminded.car_rest_service.entities.Category;
import com.foxminded.car_rest_service.entities.Manufacturer;
import com.foxminded.car_rest_service.entities.Model;
import com.foxminded.car_rest_service.exceptions.custom.*;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarDTO;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarWithoutCategoriesDTO;
import com.foxminded.car_rest_service.mapstruct.mapper.CarMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class CarServiceImp implements CarService {

    @Autowired
    private CarDAO carDAO;

    @Autowired
    private ManufacturerDAO manufacturerDAO;

    @Autowired
    private ModelDAO modelDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private CarMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<CarDTO> getAllCars(Pageable pageable) {
        log.info("GetAllCars started");

        return carDAO.findAllCarsWithAllInfo(pageable).stream()
                                                      .map(car -> mapper.carToCarDTO(car))
                                                      .collect(toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarDTO> getAllCarsByManufacturer(String manufacturer, Pageable pageable) {
        log.info("GetAllCarsByManufacturer started with: {}", manufacturer);

        return carDAO.findCarsByManufacturer(manufacturer, pageable).stream()
                                                                    .map(car -> mapper.carToCarDTO(car))
                                                                    .collect(toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarDTO> getAllCarsByManufacturerAndMinYear(String manufacturer, Integer year, Pageable pageable) {
        log.info("GetAllCarsByManufacturerAndMinYear started with: {}, year: {}", manufacturer, year);

        return carDAO.findCarsByManufacturerAndMinYear(manufacturer, year, pageable).stream()
                                                                                    .map(car -> mapper.carToCarDTO(car))
                                                                                    .collect(toList());
    }

    @Override
    public CarWithoutCategoriesDTO createCar(String manufacturerInput, String modelInput, Integer year) {
        log.info("CreateCar started with manufacturer: {}, model: {}, year: {}", manufacturerInput, modelInput, year);

        carDAO.findByManufacturerAndModelAndYear(manufacturerInput, modelInput, year).ifPresent((c -> {
            var exception =
                    new DataAlreadyExistException(format("Car with manufacturer:%s, model:%s, year:%d, already exist",
                            manufacturerInput, modelInput, year));
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        }));

        Manufacturer manufacturer = manufacturerDAO.findByNameAndYear(manufacturerInput, year).orElseThrow(() -> {
            var exception = new DataNotFoundException(format("Manufacturer with name(%s) and year(%d) wasn't found", manufacturerInput, year));
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        });

        Model model = modelDAO.findByName(modelInput).orElseThrow(() -> {
            var exception = new DataNotFoundException(format("Model with name(%s) wasn't found", modelInput));
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        });

        Car car = carDAO.save(Car.builder()
                .manufacturer(manufacturer)
                .model(model)
                .build());

        return mapper.carToCarWithoutCategoriesDTO(car);
    }

    @Override
    public void deleteCarById(Long id) {
        log.info("DeleteCarById started with id: {}", id);

        Car car = carDAO.findCarWithCategoriesById(id).orElseThrow(() -> {
            var exception = new DataNotFoundException(format("Car with id(%d) wasn't found", id));
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        });

        carDAO.delete(car);
    }

    @Override
    @Transactional
    public CarDTO addCarToCategory(Long id, String name) {
        log.info("AddCarToCategory started with id: {}, name: {}", id, name);

        Car car = caheckIfCarExists(id);

        Category category = checkIfCategoryExist(name);

        for (CarCategoryInfo next : car.getCarCategories()) {
            if (next.getCategory().getCategory().equals(name)) {
                throw new DataAlreadyExistException(format("Car already has category: %s", name));
            }
        }

        CarCategoryInfo categoryInfo = CarCategoryInfo.builder()
                                                      .car(car)
                                                      .category(category)
                                                      .build();
        car.getCarCategories().add(categoryInfo);
        category.getCarCategories().add(categoryInfo);

        return mapper.carToCarDTO(car);
    }

    @Override
    @Transactional
    public CarDTO removeCarFromCategory(Long id, String name) {
        log.info("RemoveCarFromCategory started with id: {}, name: {}", id, name);

        Car car = caheckIfCarExists(id);

        Category category = checkIfCategoryExist(name);

        var iterator = car.getCarCategories().iterator();

        while (iterator.hasNext()) {
            CarCategoryInfo next = iterator.next();
            if (next.getCar().equals(car) && next.getCategory().equals(category)) {
                iterator.remove();
                next.getCategory().getCarCategories().remove(next);
                next.setCar(null);
                next.setCategory(null);
            }
        }

        return mapper.carToCarDTO(car);
    }

    private Category checkIfCategoryExist(String name) {
        return categoryDAO.findCategoryWithCarsByName(name).orElseThrow(() -> {
                    var exception = new DataNotFoundException(format("Category with name (%s) was found", name));
                    log.error("Exception occurred during request processing: ", exception);
                    throw exception;
                }
        );
    }

    private Car caheckIfCarExists(Long id) {
        return carDAO.findCarWithAllInfoById(id).orElseThrow(() -> {
            var exception = new DataNotFoundException(format("Car with id(%d) wasn't found", id));
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        });
    }
}
