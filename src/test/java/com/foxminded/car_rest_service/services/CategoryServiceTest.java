package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.entities.Category;
import com.foxminded.car_rest_service.exceptions.custom.DataAlreadyExistException;
import com.foxminded.car_rest_service.exceptions.custom.DataNotFoundException;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarWithoutCategoriesDTO;
import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryServiceTest extends TestcontainersConfig {

    @Autowired
    private CategoryService categoryService;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void getAllCategories_shouldReturnListOfCategoryBasicDTO_whenCategoriesExist() {
        PageRequest pageable = PageRequest.of(0, 3, Sort.by("category").ascending());
        List<CategoryBasicDTO> expected = getCategories();
        List<CategoryBasicDTO> actual = categoryService.getAllCategories(pageable);

        assertEquals(expected, actual);
    }

    @Test
    void getCategoryWithCarsByName_shouldReturnCategoryDTO_whenCategoryExists() {
        CategoryDTO expected = getCategoryWithCars();

        CategoryDTO actual = categoryService.getCategoryWithCarsByName("Convertible");

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertEquals(expected.getCars(), actual.getCars())
        );

    }

    @Test
    void getCategoryWithCarsByName_shouldReturnEmptyCategory_whenCategoryNotExists() {
        CategoryDTO actual = categoryService.getCategoryWithCarsByName("INPUT");

        assertAll(
                () -> assertNull(actual.getId()),
                () -> assertNull(actual.getCategory()),
                () -> assertTrue(actual.getCars().isEmpty())
        );
    }

    @Test
    void createCategory_shouldReturnCategoryWithId_whenCategoryWasCreated() {
        CategoryBasicDTO expected = getBasicCategory(4L, "NEW");

        CategoryBasicDTO actual = categoryService.createCategory(getBasicCategory(null, "NEW"));
        
        assertEquals(expected, actual);
    }

    @Test
    void createCategory_shouldThrowDataAlreadyExistException_whenCategoryExists() {
        assertThrows(DataAlreadyExistException.class, () -> categoryService.createCategory(getBasicCategory(null, "Wagon")));
    }

    @Test
    void updateCategory_shouldReturnUpdatedCategory_whenInputCategoryExists() {
        CategoryBasicDTO expected = getBasicCategory(1L, "new");

        CategoryBasicDTO actual = categoryService.updateCategory(1L, expected);

        assertEquals(expected, actual);
    }

    @Test
    void updateCategory_shouldThrowDataNotFoundException_whenInputCategoryNotExists() {
        assertThrows(DataNotFoundException.class, () -> categoryService.updateCategory(4L, getBasicCategory(4L, "new")));
    }
    @Test
    void deleteCategoryByName_shouldDeleteCategory_whenCategoryWithInputNameExists() {
        categoryService.deleteCategoryByName("Wagon");

        Category actual = entityManager.find(Category.class, 3L);

        assertNull(actual);
    }

    @Test
    void deleteCategoryByName_shouldThrowDataNotFoundException_whenCategoryWithInputNameNotExist() {
        assertThrows(DataNotFoundException.class, () -> categoryService.deleteCategoryByName("NEW"));
    }

    private List<CategoryBasicDTO> getCategories() {
        return List.of(
                new CategoryBasicDTO(2L, "Convertible"),
                new CategoryBasicDTO(1L, "SUV1992"),
                new CategoryBasicDTO(3L, "Wagon")
        );
    }

    private CategoryDTO getCategoryWithCars() {
        Set<CarWithoutCategoriesDTO> cars = new HashSet<>();
        CarWithoutCategoriesDTO c1 = new CarWithoutCategoriesDTO();
        c1.setId(1L);
        cars.add(c1);

        CarWithoutCategoriesDTO c2 = new CarWithoutCategoriesDTO();
        c2.setId(5L);
        cars.add(c2);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(2L);
        categoryDTO.setCategory("Convertible");
        categoryDTO.setCars(cars);

        return categoryDTO;
    }

    private CategoryBasicDTO getBasicCategory(Long id, String name) {
        CategoryBasicDTO categoryBasicDTO = new CategoryBasicDTO();
        categoryBasicDTO.setId(id);
        categoryBasicDTO.setCategory(name);
        return categoryBasicDTO;
    }
}
