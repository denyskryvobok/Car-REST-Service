package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.entities.Car;
import com.foxminded.car_rest_service.exceptions.custom.DataAlreadyExistException;
import com.foxminded.car_rest_service.exceptions.custom.DataNotFoundException;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarDTO;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarWithoutCategoriesDTO;
import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelBasicDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CarServiceTest extends TestcontainersConfig {

    @Autowired
    private CarService carService;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void getAllCars_shouldReturnListOfCarDTO_whenCarsExist() {
        PageRequest pageable = PageRequest.of(0, 3, Sort.by("id").ascending());

        List<CarDTO> expected = getCars();

        List<CarDTO> actual = carService.getAllCars(pageable);

        assertEquals(expected, actual);
    }

    @Test
    void getAllCarsByManufacturer_ShouldReturnListOfCarsWithManufacturers_whenCarsWithInputManufacturesExist() {
        PageRequest pageable = PageRequest.of(0, 3, Sort.by("id").ascending());

        List<CarDTO> expected = getCarsWithManufacturers();

        List<CarDTO> actual = carService.getAllCarsByManufacturer("Acura", pageable);

        List<ManufacturerBasicDTO> manExpected = expected.stream()
                .map(CarDTO::getManufacturer)
                .collect(Collectors.toList());

        List<ManufacturerBasicDTO> manActual = actual.stream()
                .map(CarDTO::getManufacturer)
                .collect(Collectors.toList());

        assertEquals(expected, actual);
        assertEquals(manExpected, manActual);
    }

    @Test
    void getAllCarsByManufacturerAndMinYear_shouldReturnCarDTOs_whenManufacturerWithInputNameAndYearExists() {
        PageRequest pageable = PageRequest.of(0, 2, Sort.by("id").ascending());
        List<CarDTO> expected = getCarsWithManufacturersByNameAndYear();

        List<CarDTO> actual = carService.getAllCarsByManufacturerAndMinYear("Acura", 2006, pageable);

        List<ManufacturerBasicDTO> manExpected = expected.stream()
                .map(CarDTO::getManufacturer)
                .collect(Collectors.toList());

        List<ManufacturerBasicDTO> manActual = actual.stream()
                .map(CarDTO::getManufacturer)
                .collect(Collectors.toList());

        assertEquals(expected, actual);
        assertEquals(manExpected, manActual);


    }

    @Test
    void createCar_shouldReturnCreatedCarWithoutCategoriesDTO_whenInputManufacturerAndModelExist() {
        CarWithoutCategoriesDTO expected = getCarWithoutCategoriesDTO();

        CarWithoutCategoriesDTO actual = carService.createCar("Acura", "Regal", 2017);

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertEquals(expected.getManufacturer(), actual.getManufacturer()),
                () -> assertEquals(expected.getModel(), actual.getModel())
        );

    }

    @Test
    void createCar_shouldThrowDataAlreadyExistException_whenCarWithInputParametersAlreadyExists() {
        assertThrows(DataAlreadyExistException.class,
                () -> carService.createCar("Acura", "Touareg 2", 2017));
    }

    @Test
    void createCar_shouldThrowDataNotFoundException_whenInputManufacturerNameWasNOtFound() {
        assertThrows(DataNotFoundException.class,
                () -> carService.createCar("INPUT", "Touareg 2", 2017));
    }

    @Test
    void createCar_shouldThrowDataNotFoundException_whenInputModelNameWasNOtFound() {
        assertThrows(DataNotFoundException.class,
                () -> carService.createCar("Acura", "INPUT", 2017));
    }

    @Test
    void deleteCarById_shouldDeleteCar_whenCarWithInputIdExists() {
        carService.deleteCarById(1L);

        Car actual = entityManager.find(Car.class, 1L);

        assertNull(actual);
    }

    @Test
    void deleteCarById_shouldThrowDataNotFoundException_whenCarWithInputIdNotExist() {
        assertThrows(DataNotFoundException.class, () -> carService.deleteCarById(10L));
    }

    @Test
    void addCarToCategory_shouldAddCategoryAndReturnCarDTOWithCategories_whenCategoryWithInputNameExist() {
        CarDTO expected = getCarDtOWithCategoriesAfterAddCategory();
        CarDTO actual = carService.addCarToCategory(1L, "Wagon");

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertEquals(expected.getCarCategories(), actual.getCarCategories())
        );
    }

    @Test
    void addCarToCategory_shouldThrowDataAlreadyExistException_whenCarAlreadyHasInputCategory() {
        assertThrows(DataAlreadyExistException.class, () -> carService.addCarToCategory(1L, "Convertible"));
    }
    @Test
    void addCarToCategory_shouldThrowDataNotFoundException_whenCarWithInputIdNotFound() {
        assertThrows(DataNotFoundException.class, () -> carService.addCarToCategory(10L, "Wagon"));
    }

    @Test
    void addCarToCategory_shouldThrowDataNotFoundException_whenCategoryWithInputNameNotFound() {
        assertThrows(DataNotFoundException.class, () -> carService.addCarToCategory(1L, "INPUT"));
    }

    @Test
    void removeCarFromCategory_shouldRemoveCarFromCategoryAndReturnCarDto_whenCategoryWithInputNameExist() {
        CarDTO expected = getCarDtOWithCategoriesAfterDeleteCategory();

        CarDTO actual = carService.removeCarFromCategory(1L, "Convertible");

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertEquals(expected.getCarCategories(), actual.getCarCategories())
        );
    }

    @Test
    void removeCarFromCategory_shouldThrowDataNotFoundException_whenCarWithInputIdNotFound() {
        assertThrows(DataNotFoundException.class, () -> carService.removeCarFromCategory(10L, "Convertible"));
    }

    @Test
    void removeCarFromCategory_shouldThrowDataNotFoundException_whenCategoryWithInputNameNotFound() {
        assertThrows(DataNotFoundException.class, () -> carService.removeCarFromCategory(1L, "INPUT"));
    }

    private List<CarDTO> getCars() {
        CarDTO c1 = new CarDTO();
        c1.setId(1L);

        CarDTO c2 = new CarDTO();
        c2.setId(2L);

        CarDTO c3 = new CarDTO();
        c3.setId(3L);

        return List.of(c1, c2, c3);
    }

    private List<CarDTO> getCarsWithManufacturers() {
        CarDTO c1 = new CarDTO();
        c1.setId(1L);
        c1.setManufacturer(new ManufacturerBasicDTO(1L, "Acura", 2017));

        CarDTO c2 = new CarDTO();
        c2.setId(2L);
        c2.setManufacturer(new ManufacturerBasicDTO(2L, "Acura", 2005));

        CarDTO c3 = new CarDTO();
        c3.setId(3L);
        c3.setManufacturer(new ManufacturerBasicDTO(3L, "Acura", 2006));

        return List.of(c1, c2, c3);
    }

    private List<CarDTO> getCarsWithManufacturersByNameAndYear() {
        CarDTO c1 = new CarDTO();
        c1.setId(1L);
        c1.setManufacturer(new ManufacturerBasicDTO(1L, "Acura", 2017));

        CarDTO c3 = new CarDTO();
        c3.setId(3L);
        c3.setManufacturer(new ManufacturerBasicDTO(3L, "Acura", 2006));

        return List.of(c1, c3);
    }

    private CarWithoutCategoriesDTO getCarWithoutCategoriesDTO() {
        CarWithoutCategoriesDTO carWithoutCategoriesDTO = new CarWithoutCategoriesDTO();

        carWithoutCategoriesDTO.setId(8L);
        carWithoutCategoriesDTO.setManufacturer(new ManufacturerBasicDTO(1L, "Acura", 2017));
        carWithoutCategoriesDTO.setModel(new ModelBasicDTO(2L,"Regal"));

        return carWithoutCategoriesDTO;
    }

    private CarDTO getCarDtOWithCategoriesAfterAddCategory() {
        CarDTO carDTO = new CarDTO();
        carDTO.setId(1L);

        Set<CategoryBasicDTO> categories = new HashSet<>();
        categories.add(new CategoryBasicDTO(1L, "SUV1992"));
        categories.add(new CategoryBasicDTO(2L, "Convertible"));
        categories.add(new CategoryBasicDTO(3L, "Wagon"));
        carDTO.setCarCategories(categories);

        return carDTO;
    }

    private CarDTO getCarDtOWithCategoriesAfterDeleteCategory() {
        CarDTO carDTO = new CarDTO();
        carDTO.setId(1L);

        Set<CategoryBasicDTO> categories = new HashSet<>();
        categories.add(new CategoryBasicDTO(1L, "SUV1992"));
        carDTO.setCarCategories(categories);

        return carDTO;
    }
}
