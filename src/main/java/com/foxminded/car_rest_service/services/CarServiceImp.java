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
import com.foxminded.car_rest_service.exceptions.DataAlreadyExistException;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarDTO;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarWithoutCategoriesDTO;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelBasicDTO;
import com.foxminded.car_rest_service.mapstruct.mapper.CarMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    @Transactional
    public CarWithoutCategoriesDTO updateCar(CarWithoutCategoriesDTO carWithoutCategoriesDTO) {
        log.info("UpdateCar started");

        carDAO.findByManufacturerAndModelAndYear(carWithoutCategoriesDTO.getManufacturer().getManufacturer(),
                carWithoutCategoriesDTO.getModel().getModel(),
                carWithoutCategoriesDTO.getManufacturer().getYear()).ifPresent(car -> {
                    throw new DataAlreadyExistException(format(
                            "Car with manufacturer(%s), model(%s), and year(%d) already exists",
                            car.getManufacturer().getManufacturer(), car.getModel().getModel(), car.getManufacturer().getYear()
                    ));
        });

        return carDAO.findById(carWithoutCategoriesDTO.getId())
                .map(car -> {

                    ManufacturerBasicDTO manufacturerDTO = carWithoutCategoriesDTO.getManufacturer();
                    Manufacturer manufacturer = manufacturerDAO.findById(manufacturerDTO.getId())
                            .orElseGet(() -> {
                                Manufacturer newManufacturer = new Manufacturer();
                                newManufacturer.setManufacturer(manufacturerDTO.getManufacturer());
                                newManufacturer.setYear(manufacturerDTO.getYear());
                                return manufacturerDAO.save(newManufacturer);
                            });

                    ModelBasicDTO modelDTO = carWithoutCategoriesDTO.getModel();
                    Model model = modelDAO.findById(modelDTO.getId()).orElseGet(() -> {
                        Model newModel = new Model();
                        newModel.setModel(modelDTO.getModel());
                        return modelDAO.save(newModel);
                    });

                    car.setModel(model);
                    car.setManufacturer(manufacturer);

                    return mapper.carToCarWithoutCategoriesDTO(car);
                }).orElse(null);
    }

    @Override
    @Transactional
    public CarWithoutCategoriesDTO createCar(String manufacturerInput, String modelInput, Integer year) {
        log.info("CreateCar started with manufacturer: {}, model: {}, year: {}", manufacturerInput, modelInput, year);

        if (carDAO.findByManufacturerAndModelAndYear(manufacturerInput, modelInput, year).isPresent()) {
            return null;
        }

        Manufacturer manufacturer = manufacturerDAO.findByNameAndYear(manufacturerInput, year)
                .orElseGet(() -> {
                    Manufacturer m = new Manufacturer();
                    m.setManufacturer(manufacturerInput);
                    m.setYear(year);
                    return manufacturerDAO.save(m);
                });

        Model model = modelDAO.findByName(modelInput).orElseGet(() -> {
            Model m = new Model();
            m.setModel(modelInput);
            return modelDAO.save(m);
        });

        Car car = carDAO.save(Car.builder()
                .manufacturer(manufacturer)
                .model(model)
                .build());

        return mapper.carToCarWithoutCategoriesDTO(car);
    }

    @Override
    public boolean deleteCarById(Long id) {
        log.info("DeleteCarById started with id: {}", id);

        return carDAO.findCarWithCategoriesById(id)
                .map(car -> {
                    carDAO.delete(car);
                    return true;
                }).orElse(false);
    }

    @Override
    @Transactional
    public CarDTO addCarToCategory(Long id, String name) {
        log.info("AddCarToCategory started with id: {}, name: {}", id, name);

        Car car = carDAO.findCarWithAllInfoById(id)
                .map(c -> {
                    Category category = categoryDAO.findCategoryWithCarsByName(name).orElseGet(() -> {
                        Category cate = new Category();
                        cate.setCategory(name);
                        return categoryDAO.save(cate);
                    });

                    addCategory(c, category);
                    return c;
                })
                .orElse(null);

        return mapper.carToCarDTO(car);
    }

    @Override
    @Transactional
    public CarDTO removeCarFromCategory(Long id, String name) {
        log.info("RemoveCarFromCategory started with id: {}, name: {}", id, name);

        Car car = carDAO.findCarWithAllInfoById(id)
                .map(c -> {
                    Optional<Category> op = categoryDAO.findCategoryWithCarsByName(name);
                    if (op.isEmpty()) {
                        return c;
                    }
                    Category category = op.get();

                    removeCategory(c, category);

                    return c;
                })
                .orElse(null);

        return mapper.carToCarDTO(car);
    }

    private void removeCategory(Car c, Category category) {
        var iterator = c.getCarCategories().iterator();
        while (iterator.hasNext()) {
            CarCategoryInfo next = iterator.next();
            if (next.getCar().equals(c) && next.getCategory().equals(category)) {
                iterator.remove();
                next.getCategory().getCarCategories().remove(next);
                next.setCar(null);
                next.setCategory(null);
            }
        }
    }

    private void addCategory(Car c, Category category) {
        CarCategoryInfo categoryInfo = CarCategoryInfo.builder()
                .car(c)
                .category(category)
                .build();
        c.getCarCategories().add(categoryInfo);
        category.getCarCategories().add(categoryInfo);
    }
}
