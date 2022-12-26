package com.foxminded.car_rest_service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.car_rest_service.dao.AppUserDAO;
import com.foxminded.car_rest_service.exceptions.response.ResultModel;
import com.foxminded.car_rest_service.exceptions.response.ValidationErrorResponse;
import com.foxminded.car_rest_service.exceptions.response.Violation;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarDTO;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarWithoutCategoriesDTO;
import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelBasicDTO;
import com.foxminded.car_rest_service.security.SecurityConfig;
import com.foxminded.car_rest_service.services.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@MockBean(classes = AppUserDAO.class)
@WebMvcTest(controllers = CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllCars_shouldReturnCarDTOs_whenCarsExist() throws Exception {
        List<CarDTO> cars = getCars();
        when(carService.getAllCars(any(Pageable.class))).thenReturn(cars);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setData(cars);

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    void getAllCars_shouldReturnStatus404_whenCarsNotExist() throws Exception {
        when(carService.getAllCars(any(Pageable.class))).thenReturn(List.of());

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Cars not found");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    void getAllCarsByManufacturer_shouldReturn_whenCarsWithInputManufacturerExist() throws Exception {
        List<CarDTO> cars = getCarsWithManufacturers();
        when(carService.getAllCarsByManufacturer(anyString(), any(Pageable.class))).thenReturn(cars);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/cars/get/manufacturer")
                        .param("manufacturer", "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setData(cars);

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    void getAllCarsByManufacturer_shouldReturnStatus404_whenCarsWithInputManufacturerNotExist() throws Exception {
        when(carService.getAllCarsByManufacturer(anyString(), any(Pageable.class))).thenReturn(List.of());

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/cars/get/manufacturer")
                        .param("manufacturer", "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Cars with manufacturer(name) not found");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    void getAllCarsByManufacturerAndMinYear_shouldReturnCarDTOs_whenCarsWithInputManufacturerExist() throws Exception {

        List<CarDTO> cars = getCarsWithManufacturersByNameAndYear();
        when(carService.getAllCarsByManufacturerAndMinYear(anyString(), anyInt(), any(Pageable.class))).thenReturn(cars);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/cars/get/manufacturer/year")
                        .param("manufacturer", "name")
                        .param("year", "2000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setData(cars);

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    void getAllCarsByManufacturerAndMinYear_shouldReturnStatus404_whenCarsWithInputManufacturerNotExist() throws Exception {
        when(carService.getAllCarsByManufacturerAndMinYear(anyString(), anyInt(), any(Pageable.class))).thenReturn(List.of());

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/cars/get/manufacturer/year")
                        .param("manufacturer", "name")
                        .param("year", "2000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Cars with manufacturer(name) and year(2000) not found");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void createCar_shouldReturnCarWithoutCategoriesDTO_whenCarCreated() throws Exception {
        CarWithoutCategoriesDTO car = getCarWithoutCategoriesDTO();
        when(carService.createCar(anyString(), anyString(), anyInt())).thenReturn(car);

        MvcResult mvcResult = mockMvc.perform(
                        post("/api/v1/cars/manufacturer/{manufacturer}/model/{model}/year/{year}",
                                "manufacturer", "model", 2000)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setData(car);

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void createCar_shouldReturnStatus422_whenCarAlreadyExist() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/cars/manufacturer/{manufacturer}/model/{model}/year/{year}",
                        "manufacturer", "model", 2000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Car with manufacturer:manufacturer, model:model, year:2000, already exist");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
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
    @WithMockUser
    void updateCar_shouldReturnStatus200_whenInputCarExist() throws Exception {
        CarWithoutCategoriesDTO dto = getCarWithoutCategoriesDTOForUpdate();

        when(carService.updateCar(any(CarWithoutCategoriesDTO.class))).thenReturn(dto);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setData(dto);

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void updateCar_shouldReturnStatus422_whenManufacturerIsNull() throws Exception {
        CarWithoutCategoriesDTO dto = getCarWithoutCategoriesDTOWithoutManufacturer();
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        ValidationErrorResponse error = new ValidationErrorResponse();
        error.getViolations().add(new Violation("manufacturer", "must not be null"));

        String expected = objectMapper.writeValueAsString(error);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void updateCar_shouldReturnStatus404_whenCarIsNotFound() throws Exception {
        CarWithoutCategoriesDTO dto = getCarWithoutCategoriesDTOForUpdate();
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Car with id(1) not found");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void deleteById_shouldReturnStatus204_whenCarWasDeleted() throws Exception {
        when(carService.deleteCarById(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/api/v1/cars//delete/id/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void deleteById_shouldReturnStatus404_whenCarWasNotFound() throws Exception {
        when(carService.deleteCarById(anyLong())).thenReturn(false);

        MvcResult mvcResult = mockMvc.perform(delete("/api/v1/cars/delete/id/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Car with id(1) wasn't found");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void addCarToCategory_shouldReturnCarDTO_whenCarWasAddedToInputCategory() throws Exception {
        CarDTO car = getCarDtOWithCategoriesAfterAddCategory();

        when(carService.addCarToCategory(anyLong(), anyString())).thenReturn(car);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/cars/add/{id}/category/{name}", 1L, "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setData(car);

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void addCarToCategory_shouldReturnStatusCode404_whenCarByInputIdNotExist() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/cars/add/{id}/category/{name}", 1L, "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Car with input id(1) wasn't found");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
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
    @WithMockUser
    void removeCarFromCategory_shouldReturnCarDto_whenCarWasRemovedFromInputCategory() throws Exception {
        CarDTO car = getCarDtOWithCategoriesAfterDeleteCategory();
        when(carService.removeCarFromCategory(anyLong(), anyString())).thenReturn(car);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/cars/remove/{id}/category/{name}", 1L, "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setData(car);
        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void removeCarFromCategory_shouldReturnStatusCode404_whenCarByInputIdNotExist() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/cars/remove/{id}/category/{name}", 1L, "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Car with input id(1) wasn't found");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
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

    private CarWithoutCategoriesDTO getCarWithoutCategoriesDTOForUpdate() {
        CarWithoutCategoriesDTO carWithoutCategoriesDTO = new CarWithoutCategoriesDTO();

        carWithoutCategoriesDTO.setId(1L);
        carWithoutCategoriesDTO.setManufacturer(new ManufacturerBasicDTO(1L, "Acura", 2017));
        carWithoutCategoriesDTO.setModel(new ModelBasicDTO(2L, "Regal"));

        return carWithoutCategoriesDTO;
    }

    private CarWithoutCategoriesDTO getCarWithoutCategoriesDTOWithoutManufacturer() {
        CarWithoutCategoriesDTO carWithoutCategoriesDTO = new CarWithoutCategoriesDTO();

        carWithoutCategoriesDTO.setId(1L);
        carWithoutCategoriesDTO.setManufacturer(null);
        carWithoutCategoriesDTO.setModel(new ModelBasicDTO(4L, "NEW"));

        return carWithoutCategoriesDTO;
    }
}
