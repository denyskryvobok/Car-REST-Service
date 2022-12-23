package com.foxminded.car_rest_service.integration;

import com.foxminded.car_rest_service.exceptions.response.ErrorResponse;
import com.foxminded.car_rest_service.exceptions.response.ValidationErrorResponse;
import com.foxminded.car_rest_service.exceptions.response.Violation;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarDTO;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarWithoutCategoriesDTO;
import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelBasicDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CarControllerIntegrationTest extends IntegrationTestcontainersConfig {

    @Test
    void getAllCars_shouldReturnCarDTOs_whenCarsExist() throws Exception {
        List<CarDTO> cars = getCars();

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/cars")
                        .param("size", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String expected = objectMapper.writeValueAsString(cars);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    void getAllCarsByManufacturer_shouldReturnCarDTOs_whenCarsWithInputManufacturerExist() throws Exception {
        List<CarDTO> cars = getCars();

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/cars/get/manufacturer")
                        .param("manufacturer", "Acura")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String expected = objectMapper.writeValueAsString(cars);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    void getAllCarsByManufacturerAndMinYear_shouldReturnCarDTOs_whenCarsWithInputManufacturerExist() throws Exception {
        List<CarDTO> cars = getCarsWithManufacturersByNameAndYear();

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/cars/get/manufacturer/year")
                        .param("manufacturer", "Acura")
                        .param("year", "2006")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String expected = objectMapper.writeValueAsString(cars);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("jamessmith")
    void createCar_shouldReturnCarWithoutCategoriesDTO_whenCarCreated() throws Exception {
        CarWithoutCategoriesDTO car = getCarWithoutCategoriesDTO();

        MvcResult mvcResult = mockMvc.perform(
                        post("/api/v1/cars/manufacturer/{manufacturer}/model/{model}/year/{year}",
                                "Acura", "Regal", 2017)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String expected = objectMapper.writeValueAsString(car);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("jamessmith")
    void createCar_shouldReturnStatus422_whenDataAlreadyExistExceptionThrown() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/cars/manufacturer/{manufacturer}/model/{model}/year/{year}",
                        "Acura", "Touareg 2", 2017)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        ErrorResponse errorResponse = new ErrorResponse(422, "Car with manufacturer:Acura, model:Touareg 2, year:2017, already exist");

        String expected = objectMapper.writeValueAsString(errorResponse);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("jamessmith")
    void createCar_shouldReturnStatus400_whenConstraintViolationExceptionThrown() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/cars/manufacturer/{manufacturer}/model/{model}/year/{year}",
                        "  ", "model", 2000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationErrorResponse error = new ValidationErrorResponse();
        error.getViolations().add(new Violation("createCar.manufacturer", "must not be blank"));

        String expected = objectMapper.writeValueAsString(error);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("jamessmith")
    void deleteById_shouldReturnStatus204_whenCarWasDeleted() throws Exception {
        mockMvc.perform(delete("/api/v1/cars//delete/id/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails("jamessmith")
    void deleteById_shouldReturnStatus404_whenCarWasNotFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/api/v1/cars//delete/id/{id}", 10L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ErrorResponse errorResponse = new ErrorResponse(404, "Car with id(10) wasn't found");

        String expected = objectMapper.writeValueAsString(errorResponse);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("jamessmith")
    void addCarToCategory_shouldReturnCarDTO_whenCarWasAddedToInputCategory() throws Exception {
        CarDTO car = getCarDtOWithCategoriesAfterAddCategory();

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/cars/add/{id}/category/{name}", 1L, "Wagon")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String expected = objectMapper.writeValueAsString(car);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("jamessmith")
    void addCarToCategory_shouldReturnStatusCode404_whenInputCategoryNotExist() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/cars/add/{id}/category/{name}", 1L, "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ErrorResponse errorResponse = new ErrorResponse(404, "Category with name (name) was found");

        String expected = objectMapper.writeValueAsString(errorResponse);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("jamessmith")
    void addCarToCategory_shouldReturnStatus400_whenConstraintViolationExceptionThrown() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/cars/add/{id}/category/{name}", 1L, "  ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationErrorResponse error = new ValidationErrorResponse();
        error.getViolations().add(new Violation("addCarToCategory.name", "must not be blank"));

        String expected = objectMapper.writeValueAsString(error);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("jamessmith")
    void removeCarFromCategory_shouldReturnCarDto_whenCarWasRemovedFromInputCategory() throws Exception {
        CarDTO car = getCarDtOWithCategoriesAfterDeleteCategory();

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/cars/remove/{id}/category/{name}", 1L, "SUV1992")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String expected = objectMapper.writeValueAsString(car);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("jamessmith")
    void removeCarFromCategory_shouldReturnStatusCode404_whenInputCategoryNotExist() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/cars/remove/{id}/category/{name}", 1L, "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ErrorResponse errorResponse = new ErrorResponse(404, "Category with name (name) was found");

        String expected = objectMapper.writeValueAsString(errorResponse);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("jamessmith")
    void removeCarFromCategory_shouldReturnStatus400_whenConstraintViolationExceptionThrown() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/cars/remove/{id}/category/{name}", 1L, "  ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationErrorResponse error = new ValidationErrorResponse();
        error.getViolations().add(new Violation("removeCarFromCategory.name", "must not be blank"));

        String expected = objectMapper.writeValueAsString(error);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    private List<CarDTO> getCars() {
        CarDTO c1 = new CarDTO();

        Set<CategoryBasicDTO> s1 = new LinkedHashSet<>();
        s1.add(new CategoryBasicDTO(1L, "SUV1992"));
        s1.add(new CategoryBasicDTO(2L, "Convertible"));

        c1.setModel(new ModelBasicDTO(1L, "Touareg 2"));
        c1.setManufacturer(new ManufacturerBasicDTO(1L, "Acura", 2017));
        c1.setCarCategories(s1);
        c1.setId(1L);

        CarDTO c2 = new CarDTO();
        c2.setModel(new ModelBasicDTO(1L, "Touareg 2"));
        c2.setManufacturer(new ManufacturerBasicDTO(2L, "Acura", 2005));
        c2.setCarCategories(Set.of(new CategoryBasicDTO(1L, "SUV1992")));
        c2.setId(2L);

        CarDTO c3 = new CarDTO();
        c3.setModel(new ModelBasicDTO(2L, "Regal"));
        c3.setManufacturer(new ManufacturerBasicDTO(3L, "Acura", 2006));
        c3.setCarCategories(Set.of(new CategoryBasicDTO(3L, "Wagon")));
        c3.setId(3L);

        return List.of(c1, c2, c3);
    }

    private List<CarDTO> getCarsWithManufacturersByNameAndYear() {
        CarDTO c1 = new CarDTO();

        Set<CategoryBasicDTO> s1 = new LinkedHashSet<>();
        s1.add(new CategoryBasicDTO(1L, "SUV1992"));
        s1.add(new CategoryBasicDTO(2L, "Convertible"));

        c1.setModel(new ModelBasicDTO(1L, "Touareg 2"));
        c1.setManufacturer(new ManufacturerBasicDTO(1L, "Acura", 2017));
        c1.setCarCategories(s1);
        c1.setId(1L);

        CarDTO c3 = new CarDTO();
        c3.setModel(new ModelBasicDTO(2L, "Regal"));
        c3.setManufacturer(new ManufacturerBasicDTO(3L, "Acura", 2006));
        c3.setCarCategories(Set.of(new CategoryBasicDTO(3L, "Wagon")));
        c3.setId(3L);

        return List.of(c1, c3);
    }

    private CarWithoutCategoriesDTO getCarWithoutCategoriesDTO() {
        CarWithoutCategoriesDTO carWithoutCategoriesDTO = new CarWithoutCategoriesDTO();

        carWithoutCategoriesDTO.setId(8L);
        carWithoutCategoriesDTO.setManufacturer(new ManufacturerBasicDTO(1L, "Acura", 2017));
        carWithoutCategoriesDTO.setModel(new ModelBasicDTO(2L, "Regal"));

        return carWithoutCategoriesDTO;
    }

    private CarDTO getCarDtOWithCategoriesAfterAddCategory() {
        CarDTO carDTO = new CarDTO();
        carDTO.setModel(new ModelBasicDTO(1L, "Touareg 2"));
        carDTO.setManufacturer(new ManufacturerBasicDTO(1L, "Acura", 2017));
        carDTO.setId(1L);

        Set<CategoryBasicDTO> categories = new LinkedHashSet<>();
        categories.add(new CategoryBasicDTO(1L, "SUV1992"));
        categories.add(new CategoryBasicDTO(2L, "Convertible"));
        categories.add(new CategoryBasicDTO(3L, "Wagon"));
        carDTO.setCarCategories(categories);

        return carDTO;
    }

    private CarDTO getCarDtOWithCategoriesAfterDeleteCategory() {
        CarDTO carDTO = new CarDTO();
        carDTO.setModel(new ModelBasicDTO(1L, "Touareg 2"));
        carDTO.setManufacturer(new ManufacturerBasicDTO(1L, "Acura", 2017));
        carDTO.setCarCategories(Set.of(new CategoryBasicDTO(2L, "Convertible")));
        carDTO.setId(1L);

        return carDTO;
    }
}
