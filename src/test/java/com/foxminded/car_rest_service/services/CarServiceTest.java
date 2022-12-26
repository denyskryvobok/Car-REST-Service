package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.entities.Car;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void createCar_shouldReturnCreatedCarWithoutCategoriesDTO_whenInputManufacturersAndModelNotExist() {
        CarWithoutCategoriesDTO expected = getCarWithoutCategoriesDTOWhenAddNewManufacturerAndModel();

        CarWithoutCategoriesDTO actual = carService.createCar("new", "new", 2017);

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertEquals(expected.getManufacturer(), actual.getManufacturer()),
                () -> assertEquals(expected.getManufacturer().getManufacturer(), actual.getManufacturer().getManufacturer()),
                () -> assertEquals(expected.getModel().getModel(), actual.getModel().getModel()),
                () -> assertEquals(expected.getModel(), actual.getModel()));


    }

    @Test
    void createCar_shouldReturnNull_whenCarWithInputParametersAlreadyExists() {
        CarWithoutCategoriesDTO actual = carService.createCar("Acura", "Touareg 2", 2017);
        assertNull(actual);
    }

    @Test
    void updateCar_shouldReturnUpdatedCar_whenInputCarExists() {
        CarWithoutCategoriesDTO expected = getCarWithoutCategoriesDTOForUpdate();

        CarWithoutCategoriesDTO actual = carService.updateCar(expected);

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertEquals(expected.getModel(), actual.getModel()),
                () -> assertEquals(expected.getManufacturer(), actual.getManufacturer())
        );
    }

    @Test
    void updateCar_shouldReturnUpdatedCarWithNewModelAndManufacturer_whenInputCarExistsAndInputManufacturerAndModelNotExist() {
        CarWithoutCategoriesDTO expected = getCarWithoutCategoriesDTOForUpdateWithNewModelAndManufacturer();

        CarWithoutCategoriesDTO actual = carService.updateCar(expected);

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertEquals(expected.getModel().getModel(), actual.getModel().getModel()),
                () -> assertEquals(expected.getManufacturer().getManufacturer(), actual.getManufacturer().getManufacturer())
        );
    }

    @Test
    void updateCar_shouldReturnNull_whenInputCarNotExists() {
        CarWithoutCategoriesDTO actual = carService.updateCar(getCarWithoutCategoriesDTO());
        assertNull(actual);
    }

    @Test
    void deleteCarById_shouldDeleteCarAndReturnTrue_whenCarWithInputIdExists() {
        boolean isDeleted = carService.deleteCarById(1L);

        Car actual = entityManager.find(Car.class, 1L);

        assertNull(actual);
        assertTrue(isDeleted);
    }

    @Test
    void deleteCarById_shouldReturnFalse_whenCarWithInputIdNotExist() {
        boolean isDeleted = carService.deleteCarById(10L);
        assertFalse(isDeleted);
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
    void addCarToCategory_shouldAddCategoryAndReturnCarDTOWithCategories_whenCategoryWithInputNameNotExist() {
        CarDTO expected = getCarDtOWithCategoriesAfterAddCategoryWhenCategoryNotExist();
        CarDTO actual = carService.addCarToCategory(1L, "new");

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertEquals(expected.getCarCategories(), actual.getCarCategories())
        );
    }

    @Test
    void addCarToCategory_shouldReturnNull_whenCarWithInputIdNotFound() {
        CarDTO actual = carService.addCarToCategory(10L, "Wagon");
        assertNull(actual);
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
    void removeCarFromCategory_shouldReturnCarDto_whenCategoryWithInputNameNotExist() {
        CarDTO expected = getCarDtOWithCategories();

        CarDTO actual = carService.removeCarFromCategory(1L, "new");

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertEquals(expected.getCarCategories(), actual.getCarCategories())
        );
    }

    @Test
    void removeCarFromCategory_shouldReturnNull_whenCarWithInputIdNotFound() {
        CarDTO actual = carService.removeCarFromCategory(10L, "Convertible");
        assertNull(actual);
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
        carWithoutCategoriesDTO.setModel(new ModelBasicDTO(2L, "Regal"));

        return carWithoutCategoriesDTO;
    }

    private CarWithoutCategoriesDTO getCarWithoutCategoriesDTOWhenAddNewManufacturerAndModel() {
        CarWithoutCategoriesDTO carWithoutCategoriesDTO = new CarWithoutCategoriesDTO();

        carWithoutCategoriesDTO.setId(8L);
        carWithoutCategoriesDTO.setManufacturer(new ManufacturerBasicDTO(7L, "new", 2017));
        carWithoutCategoriesDTO.setModel(new ModelBasicDTO(4L, "new"));

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

    private CarDTO getCarDtOWithCategoriesAfterAddCategoryWhenCategoryNotExist() {
        CarDTO carDTO = new CarDTO();
        carDTO.setId(1L);

        Set<CategoryBasicDTO> categories = new HashSet<>();
        categories.add(new CategoryBasicDTO(1L, "SUV1992"));
        categories.add(new CategoryBasicDTO(2L, "Convertible"));
        categories.add(new CategoryBasicDTO(4L, "new"));
        carDTO.setCarCategories(categories);

        return carDTO;
    }

    private CarDTO getCarDtOWithCategories() {
        CarDTO carDTO = new CarDTO();
        carDTO.setId(1L);

        Set<CategoryBasicDTO> categories = new HashSet<>();
        categories.add(new CategoryBasicDTO(1L, "SUV1992"));
        categories.add(new CategoryBasicDTO(2L, "Convertible"));
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

    private CarWithoutCategoriesDTO getCarWithoutCategoriesDTOForUpdateWithNewModelAndManufacturer() {
        CarWithoutCategoriesDTO carWithoutCategoriesDTO = new CarWithoutCategoriesDTO();

        carWithoutCategoriesDTO.setId(1L);
        carWithoutCategoriesDTO.setManufacturer(new ManufacturerBasicDTO(7L, "NEW", 2017));
        carWithoutCategoriesDTO.setModel(new ModelBasicDTO(4L, "NEW"));

        return carWithoutCategoriesDTO;
    }

    private CarWithoutCategoriesDTO getCarWithoutCategoriesDTOForUpdate() {
        CarWithoutCategoriesDTO carWithoutCategoriesDTO = new CarWithoutCategoriesDTO();

        carWithoutCategoriesDTO.setId(1L);
        carWithoutCategoriesDTO.setManufacturer(new ManufacturerBasicDTO(1L, "Acura", 2017));
        carWithoutCategoriesDTO.setModel(new ModelBasicDTO(2L, "Regal"));

        return carWithoutCategoriesDTO;
    }
}
