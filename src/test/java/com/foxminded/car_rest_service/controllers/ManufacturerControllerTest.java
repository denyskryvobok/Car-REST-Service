package com.foxminded.car_rest_service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.car_rest_service.dao.AppUserDAO;
import com.foxminded.car_rest_service.exceptions.response.ResultModel;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

        ResultModel resultModel = new ResultModel();
        resultModel.setData(manufacturers);

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void getAllUniqueManufacturers_shouldReturnStatus404_whenManufacturersNotExist() throws Exception {
        when(manufacturerService.getAllUniqueManufacturers(any(Pageable.class))).thenReturn(List.of());

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Manufacturers not found");

        String expected = objectMapper.writeValueAsString(resultModel);

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

        ResultModel resultModel = new ResultModel();
        resultModel.setData(manufacturerDTOS);

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void getAllManufacturersByName_shouldReturnStatus404_whenManufacturersNotExist() throws Exception {
        when(manufacturerService.getAllManufacturersByName(anyString(), any(Pageable.class))).thenReturn(List.of());

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/manufacturers/get/name/{name}", "Acura")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Manufacturers with name(Acura) not found");

        String expected = objectMapper.writeValueAsString(resultModel);

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

        ResultModel resultModel = new ResultModel();
        resultModel.setData(manufacturer);

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void createManufacturer_shouldReturnStatus422_whenManufacturerAlreadyExists() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getManufacturerBasicDTO(null, "new", 2033))))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Manufacturer with name(new) and year(2033) already exist");

        String expected = objectMapper.writeValueAsString(resultModel);

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

        ResultModel resultModel = new ResultModel();
        resultModel.setData(dto);

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
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
    @WithMockUser
    void deleteAllManufacturerByName_shouldReturnStatus204_whenManufacturerWasDeleted() throws Exception {
        when(manufacturerService.deleteAllManufacturerByName(anyString())).thenReturn(true);

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
        when(manufacturerService.deleteAllManufacturerByName(anyString())).thenReturn(false);

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
    @WithMockUser
    void deleteManufacturerByNameAndYear_shouldReturnStatus204_whenManufacturerWasDeleted() throws Exception {
        when(manufacturerService.deleteManufacturerByNameAndYear(anyString(), anyInt())).thenReturn(true);

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
        when(manufacturerService.deleteManufacturerByNameAndYear(anyString(), anyInt())).thenReturn(false);

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
