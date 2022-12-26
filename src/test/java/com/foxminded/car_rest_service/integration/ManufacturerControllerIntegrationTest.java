package com.foxminded.car_rest_service.integration;

import com.foxminded.car_rest_service.exceptions.response.ResultModel;
import com.foxminded.car_rest_service.exceptions.response.ValidationErrorResponse;
import com.foxminded.car_rest_service.exceptions.response.Violation;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarWithoutManufactureDTO;
import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerDTO;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelBasicDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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

class ManufacturerControllerIntegrationTest extends IntegrationTestcontainersConfig {

    @Test
    @WithUserDetails("jamessmith")
    void getAllUniqueManufacturers_shouldReturnNamesOfManufacturers_whenManufacturersExist() throws Exception {
        List<String> manufacturers = getNamesOfManufacturers();
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setData(manufacturers);

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("jamessmith")
    void getAllManufacturersByName_shouldReturnManufactureDTOs_whenManufacturersExist() throws Exception {
        List<ManufacturerDTO> manufacturerDTOS = getAllManufacturers();

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/manufacturers/get/name/{name}", "Acura")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setData(manufacturerDTOS);

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void getAllManufacturersByName_shouldReturnStatus404_whenManufacturersNotExist() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/manufacturers/get/name/{name}", "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Manufacturers with name(name) not found");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("jamessmith")
    void createManufacturer_shouldReturnCreatedManufacturerBasicDTO_whenInputManufacturerNotExists() throws Exception {
        ManufacturerBasicDTO manufacturer = getManufacturerBasicDTO(7L, "NEW_MANUFACTURER", 2030);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getManufacturerBasicDTO(null, "NEW_MANUFACTURER", 2030))))
                .andExpect(status().isCreated())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setData(manufacturer);

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("jamessmith")
    void createManufacturer_shouldReturnStatus422_whenManufacturerAlreadyExist() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getManufacturerBasicDTO(null, "Acura", 2017))))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Manufacturer with name(Acura) and year(2017) already exist");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("jamessmith")
    void createManufacturer_shouldReturnStatus422_whenMethodArgumentNotValidExceptionWasThrown() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getManufacturerBasicDTO(null, "  ", 2000))))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        ValidationErrorResponse error = new ValidationErrorResponse();
        error.getViolations().add(new Violation("manufacturer", "must not be blank"));

        String expected = objectMapper.writeValueAsString(error);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("jamessmith")
    void updateManufacturer_shouldReturnStatus200AndReturnUpdatedManufacturer_whenManufacturerWasUpdated() throws Exception {
        ManufacturerBasicDTO dto = getManufacturerBasicDTO(1L, "new", 2000);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/manufacturers/update/by/{id}", 1)
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
    @WithUserDetails("jamessmith")
    void updateManufacturer_shouldReturnStatus404_whenManufacturerWasNotFound() throws Exception {
        ManufacturerBasicDTO dto = getManufacturerBasicDTO(8L, "new", 200);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/manufacturers/update/by/{id}", 8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Manufacturer with id(8) wasn't found");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("jamessmith")
    void deleteAllManufacturerByName_shouldReturnStatus204_whenManufacturerWasDeleted() throws Exception {
        mockMvc.perform(delete("/api/v1/manufacturers/delete/name/{name}", "Acura")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails("jamessmith")
    void deleteAllManufacturerByName_shouldReturnStatus400_whenConstraintViolationExceptionThrown() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/api/v1/manufacturers/delete/name/{name}", "  ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationErrorResponse error = new ValidationErrorResponse();
        error.getViolations().add(new Violation("deleteAllManufacturerByName.name", "must not be blank"));

        String expected = objectMapper.writeValueAsString(error);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("jamessmith")
    void deleteAllManufacturerByName_shouldReturnStatus404_whenManufacturersWasNotFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/api/v1/manufacturers/delete/name/{name}", "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Manufacturers with name(name) weren't found");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("jamessmith")
    void deleteManufacturerByNameAndYear_shouldReturnStatus204_whenManufacturerWasDeleted() throws Exception {
        mockMvc.perform(delete("/api/v1/manufacturers/delete/name/{name}/year/{year}", "Acura", 2017)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails("jamessmith")
    void deleteManufacturerByNameAndYear_shouldReturnStatus400_whenConstraintViolationExceptionThrown() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/api/v1/manufacturers/delete/name/{name}/year/{year}", "  ", 2000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationErrorResponse error = new ValidationErrorResponse();
        error.getViolations().add(new Violation("deleteManufacturerByNameAndYear.name", "must not be blank"));

        String expected = objectMapper.writeValueAsString(error);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("jamessmith")
    void deleteManufacturerByNameAndYear_shouldReturnStatus404_whenManufacturersWasNotFound() throws Exception {
        MvcResult mvcResult =
                mockMvc.perform(delete("/api/v1/manufacturers/delete/name/{name}/year/{year}", "name", 2000)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Manufacturers with name(name) and year(2000) wasn't found");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    private List<String> getNamesOfManufacturers() {
        return List.of("Acura", "Aston Martin");
    }

    private List<ManufacturerDTO> getAllManufacturers() {
        ManufacturerDTO m1 = new ManufacturerDTO();

        Set<CategoryBasicDTO> s1 = new LinkedHashSet<>();
        s1.add(new CategoryBasicDTO(1L, "SUV1992"));
        s1.add(new CategoryBasicDTO(2L, "Convertible"));

        m1.setId(1L);
        m1.setManufacturer("Acura");
        m1.setYear(2017);
        m1.setCars(Set.of(new CarWithoutManufactureDTO(1L, new ModelBasicDTO(1L, "Touareg 2"), s1)));

        ManufacturerDTO m2 = new ManufacturerDTO();
        m2.setId(2L);
        m2.setManufacturer("Acura");
        m2.setYear(2005);
        m2.setCars(Set.of(new CarWithoutManufactureDTO(2L, new ModelBasicDTO(1L, "Touareg 2"),
                Set.of(new CategoryBasicDTO(1L, "SUV1992")))));

        ManufacturerDTO m3 = new ManufacturerDTO();
        m3.setId(3L);
        m3.setManufacturer("Acura");
        m3.setYear(2006);
        m3.setCars(Set.of(new CarWithoutManufactureDTO(3L, new ModelBasicDTO(2L, "Regal"),
                Set.of(new CategoryBasicDTO(3L, "Wagon")))));

        return List.of(m1, m2, m3);
    }

    private ManufacturerBasicDTO getManufacturerBasicDTO(Long id, String name, Integer year) {
        ManufacturerBasicDTO manufacturerBasicDTO = new ManufacturerBasicDTO();
        manufacturerBasicDTO.setId(id);
        manufacturerBasicDTO.setManufacturer(name);
        manufacturerBasicDTO.setYear(year);

        return manufacturerBasicDTO;
    }
}
