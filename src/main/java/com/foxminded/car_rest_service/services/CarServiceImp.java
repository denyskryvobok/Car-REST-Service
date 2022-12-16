package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.dao.CarDAO;
import com.foxminded.car_rest_service.dao.CategoryDAO;
import com.foxminded.car_rest_service.dao.ManufacturerDAO;
import com.foxminded.car_rest_service.dao.ModelDAO;
import com.foxminded.car_rest_service.entities.*;
import com.foxminded.car_rest_service.exceptions.custom.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.format;

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

    @Override
    @Transactional(readOnly = true)
    public List<Car> getAllCars(Pageable pageable) {
        log.info("GetAllCars started");

        List<Car> cars = carDAO.findAllCarsWithAllInfo(pageable);
        if (cars.isEmpty()) {
            var exception = new CategoryNotFoundException("No car was found");
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        }

        return cars;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Car> getAllCarsByManufacturer(String manufacturer, Pageable pageable) {
        log.info("GetAllCarsByManufacturer started with: {}", manufacturer);

        List<Car> cars = carDAO.findCarsByManufacturer(manufacturer, pageable);
        if (cars.isEmpty()) {
            var exception = new ManufacturerNotFoundException("No manufacturer was found");
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        }

        return cars;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Car> getAllCarsByManufacturerAndMinYear(String manufacturer, Integer year, Pageable pageable) {
        log.info("GetAllCarsByManufacturerAndMinYear started with: {}, year: {}", manufacturer, year);

        List<Car> cars = carDAO.findCarsByManufacturerAndMinYear(manufacturer, year, pageable);
        if (cars.isEmpty()) {
            var exception = new ManufacturerNotFoundException("No manufacturer was found");
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        }

        return cars;
    }

    @Override
    public Car createCar(String manufacturerInput, String modelInput, Integer year) {
        log.info("CreateCar started with manufacturer: {}, model: {}, year: {}", manufacturerInput, modelInput, year);

        carDAO.findByManufacturerAndModelAndYear(manufacturerInput, modelInput, year).ifPresent((c -> {
            var exception =
                    new CarAlreadyExistException(format("Car with manufacturer:%s, model:%s, year:%d, already exist",
                            manufacturerInput, modelInput, year));
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        }));

        Manufacturer manufacturer = manufacturerDAO.findByNameAndYear(manufacturerInput, year).orElseThrow(() -> {
            var exception = new ManufacturerNotFoundException("No manufacturer was found");
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        });

        Model model = modelDAO.findByName(modelInput).orElseThrow(() -> {
            var exception = new ModelNotFoundException("No model was found");
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        });

        return carDAO.save(Car.builder()
                .manufacturer(manufacturer)
                .model(model)
                .build());
    }

    @Override
    public void deleteCarById(Long id) {
        log.info("DeleteCarById started with id: {}", id);

        Car car = carDAO.findCarWithCategoriesById(id).orElseThrow(() -> {
            var exception = new CarNotFoundException("No car was found");
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        });

        carDAO.delete(car);
    }

    @Override
    @Transactional
    public Car addCarToCategory(Long id, String name) {
        log.info("DeleteCarById started with id: {}", id);

        Car car = caheckCar(id);

        Category category = checkCategory(name);

        for (CarCategoryInfo next : car.getCarCategories()) {
            if (next.getCategory().getCategory().equals(name)) {
                throw new CarAlreadyHasSuchCategoryException(format("Car already has category: %s", name));
            }
        }

        CarCategoryInfo categoryInfo = CarCategoryInfo.builder()
                                                      .car(car)
                                                      .category(category)
                                                      .build();
        car.getCarCategories().add(categoryInfo);
        category.getCarCategories().add(categoryInfo);

        return car;
    }

    @Override
    @Transactional
    public Car removeCarFromCategory(Long id, String name) {
        log.info("RemoveCarFromCategory started with id: {}", id);

        Car car = caheckCar(id);

        Category category = checkCategory(name);

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


        return car;
    }

    private Category checkCategory(String name) {
        return categoryDAO.findCategoryWithCarsByName(name).orElseThrow(() -> {
                    var exception = new CategoryNotFoundException("No category was found");
                    log.error("Exception occurred during request processing: ", exception);
                    throw exception;
                }
        );
    }

    private Car caheckCar(Long id) {
        return carDAO.findCarWithAllInfoById(id).orElseThrow(() -> {
            var exception = new CarNotFoundException("No car was found");
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        });
    }
}
