package com.foxminded.car_rest_service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.car_rest_service.dao.AppUserDAO;
import com.foxminded.car_rest_service.exceptions.custom.DataAlreadyExistException;
import com.foxminded.car_rest_service.exceptions.custom.DataNotFoundException;
import com.foxminded.car_rest_service.exceptions.response.ErrorResponse;
import com.foxminded.car_rest_service.exceptions.response.ValidationErrorResponse;
import com.foxminded.car_rest_service.exceptions.response.Violation;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerDTO;
import com.foxminded.car_rest_service.security.SecurityConfig;
import com.foxminded.car_rest_service.services.ManufacturerService;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@MockBean(classes = AppUserDAO.class)
@WebMvcTest(controllers = ManufacturerController.class)
class ManufacturerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManufacturerService manufacturerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getAllUniqueManufacturers_shouldReturnNamesOfManufacturers_whenManufacturersExist() throws Exception {
        List<String> manufacturers = getNamesOfManufacturers();

        when(manufacturerService.getAllUniqueManufacturers(any(Pageable.class))).thenReturn(manufacturers);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String expected = objectMapper.writeValueAsString(manufacturers);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void getAllManufacturersByName_shouldReturnManufactureDTOs_whenManufacturersExist() throws Exception {
        List<ManufacturerDTO> manufacturerDTOS = getAllManufacturers();

        when(manufacturerService.getAllManufacturersByName(anyString(), any(Pageable.class)))
                .thenReturn(manufacturerDTOS);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/manufacturers/get/name/{name}", "Acura")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String expected = objectMapper.writeValueAsString(manufacturerDTOS);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void createManufacturer_shouldReturnCreatedManufacturerBasicDTO_whenInputManufacturerNotExists() throws Exception {
        ManufacturerBasicDTO manufacturer = getManufacturerBasicDTO(1L, "NEW_MANUFACTURER", 2030);

        when(manufacturerService.createManufacturer(any(ManufacturerBasicDTO.class)))
                .thenReturn(manufacturer);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getManufacturerBasicDTO(null, "NEW_MANUFACTURER", 2030))))
                .andExpect(status().isCreated())
                .andReturn();

        String expected = objectMapper.writeValueAsString(manufacturer);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void createManufacturer_shouldReturnStatus422_whenDataAlreadyExistExceptionThrown() throws Exception {
        when(manufacturerService.createManufacturer(any(ManufacturerBasicDTO.class)))
                .thenThrow(new DataAlreadyExistException("Manufacturer with name(new) already exists"));

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getManufacturerBasicDTO(null, "new", 2033))))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        ErrorResponse errorResponse = new ErrorResponse(422, "Manufacturer with name(new) already exists");

        String expected = objectMapper.writeValueAsString(errorResponse);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
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
    @WithMockUser
    void updateManufacturer_shouldReturnStatus200AndReturnUpdatedManufacturer_whenManufacturerWasUpdated() throws Exception {
        ManufacturerBasicDTO dto = getManufacturerBasicDTO(1L, "new", 2000);

        when(manufacturerService.updateManufacturer(1L, dto)).thenReturn(dto);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/manufacturers/update/by/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();

        String expected = objectMapper.writeValueAsString(dto);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void updateManufacturer_shouldReturnStatus404_whenManufacturerWasNotFound() throws Exception {
        ManufacturerBasicDTO dto = getManufacturerBasicDTO(8L, "new", 200);

        when(manufacturerService.updateManufacturer(anyLong(), any(ManufacturerBasicDTO.class)))
                .thenThrow(new DataNotFoundException("Manufacturer with id(8) wasn't found"));

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/manufacturers/update/by/{id}", 8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andReturn();

        ErrorResponse errorResponse = new ErrorResponse(404, "Manufacturer with id(8) wasn't found");

        String expected = objectMapper.writeValueAsString(errorResponse);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void deleteAllManufacturerByName_shouldReturnStatus204_whenManufacturerWasDeleted() throws Exception {
        mockMvc.perform(delete("/api/v1/manufacturers/delete/name/{name}", "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
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
    @WithMockUser
    void deleteAllManufacturerByName_shouldReturnStatus404_whenManufacturersWasNotFound() throws Exception {
        doThrow(new DataNotFoundException("Manufacturer with name(name) wasn't found"))
                .when(manufacturerService).deleteAllManufacturerByName("name");

        MvcResult mvcResult = mockMvc.perform(delete("/api/v1/manufacturers/delete/name/{name}", "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ErrorResponse errorResponse = new ErrorResponse(404, "Manufacturer with name(name) wasn't found");

        String expected = objectMapper.writeValueAsString(errorResponse);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void deleteManufacturerByNameAndYear_shouldReturnStatus204_whenManufacturerWasDeleted() throws Exception {
        mockMvc.perform(delete("/api/v1/manufacturers/delete/name/{name}/year/{year}", "name", 2000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
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
    @WithMockUser
    void deleteManufacturerByNameAndYear_shouldReturnStatus404_whenManufacturersWasNotFound() throws Exception {
        doThrow(new DataNotFoundException("Manufacturer with name(name) and year(2000) wasn't found"))
                .when(manufacturerService).deleteManufacturerByNameAndYear("name", 2000);

        MvcResult mvcResult =
                mockMvc.perform(delete("/api/v1/manufacturers/delete/name/{name}/year/{year}", "name", 2000)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andReturn();

        ErrorResponse errorResponse = new ErrorResponse(404, "Manufacturer with name(name) and year(2000) wasn't found");

        String expected = objectMapper.writeValueAsString(errorResponse);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    private List<String> getNamesOfManufacturers() {
        return List.of("Acura", "Aston Martin");
    }

    private List<ManufacturerDTO> getAllManufacturers() {
        ManufacturerDTO m1 = new ManufacturerDTO();
        m1.setId(1L);
        m1.setManufacturer("Acura");
        m1.setYear(2000);
        ManufacturerDTO m2 = new ManufacturerDTO();
        m2.setId(2L);
        m2.setManufacturer("Acura");
        m2.setYear(2020);
        ManufacturerDTO m3 = new ManufacturerDTO();
        m3.setId(3L);
        m3.setManufacturer("Acura");
        m3.setYear(2019);

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
