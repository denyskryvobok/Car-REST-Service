package com.foxminded.car_rest_service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.car_rest_service.exceptions.response.ResultModel;
import com.foxminded.car_rest_service.exceptions.response.ValidationErrorResponse;
import com.foxminded.car_rest_service.exceptions.response.Violation;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarWithoutModelDTO;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelDTO;
import com.foxminded.car_rest_service.services.ModelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Import(SecurityConfigTest.class)
@WebMvcTest(controllers = ModelController.class)
class ModelControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ModelService modelService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "USER")
    void getAllModels_shouldReturnStatus200WithJsonResponseBody_whenModelsExists() throws Exception {
        List<ModelBasicDTO> modelBasicDTOs = getModelBasicDTOs();

        when(modelService.getAllModels(any(Pageable.class))).thenReturn(modelBasicDTOs);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/models/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setData(modelBasicDTOs);

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);

    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllModels_shouldReturnStatus404_whenModelsNotExists() throws Exception {
        when(modelService.getAllModels(any(Pageable.class))).thenReturn(List.of());

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/models/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Models not found");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);

    }

    @Test
    @WithMockUser(roles = "USER")
    void getModelWithCarsByName_shouldReturnStatus200WithJsonResponseBody_whenModelsExists() throws Exception {
        ModelDTO modelDTO = getModelDTO();
        when(modelService.getModelWithCarsByName(anyString())).thenReturn(modelDTO);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/models/name/{name}", "Grand")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setData(modelDTO);

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getModelWithCarsByName_shouldReturnStatus404_whenModelNotExists() throws Exception {
        when(modelService.getModelWithCarsByName(anyString())).thenReturn(null);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/models/name/{name}", "Grand")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Model with name(Grand) not found");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getModelWithCarsByName_shouldReturnStatus400_whenModelsExists() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/models/name/{name}", "  ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationErrorResponse error = new ValidationErrorResponse();
        error.getViolations().add(new Violation("getModelWithCarsByName.name", "must not be blank"));

        String expected = objectMapper.writeValueAsString(error);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createModel_shouldReturnStatus201WithJsonResponseBody_whenConstraintViolationException() throws Exception {
        ModelBasicDTO dto = getModelBasicDTO(1L, "NEW");

        when(modelService.createModel(any(ModelBasicDTO.class))).thenReturn(dto);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getModelBasicDTO(null, "NEW"))))
                .andExpect(status().isCreated())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setData(dto);

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createModel_shouldReturnStatus422_whenMethodArgumentNotValidExceptionWasThrown() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getModelBasicDTO(null, "  "))))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        ValidationErrorResponse error = new ValidationErrorResponse();
        error.getViolations().add(new Violation("model", "must not be blank"));

        String expected = objectMapper.writeValueAsString(error);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createModel_shouldReturnStatus422_whenModelAlreadyExist() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getModelBasicDTO(null, "new"))))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Model with name(new) already exists");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(roles = "STAFF")
    void updateModel_shouldReturnStatus200AndReturnUpdatedModel_whenModelWasUpdated() throws Exception {
        ModelBasicDTO dto = getModelBasicDTO(1L, "new");

        when(modelService.updateModel(1L, dto)).thenReturn(dto);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/models/id/{id}", 1)
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
    @WithMockUser(roles = "STAFF")
    void updateModel_shouldReturnStatus404_whenModelWasNotFound() throws Exception {
        ModelBasicDTO dto = getModelBasicDTO(8L, "new");

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/models/id/{id}", 8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Model with id(8) wasn't found");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteModelByName_shouldReturnStatus204_whenModelWasDeleted() throws Exception {
        when(modelService.deleteModelByName(anyString())).thenReturn(true);

        mockMvc.perform(delete("/api/v1/models/name/{name}", "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteModelByName_shouldReturnStatus404_whenModelWasNotFound() throws Exception {
        when(modelService.deleteModelByName(anyString())).thenReturn(false);

        MvcResult mvcResult = mockMvc.perform(delete("/api/v1/models/name/{name}", "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Model with name(name) wasn't found");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteModelByName_shouldReturnStatus400_whenConstraintViolationExceptionThrown() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/api/v1/models/name/{name}", "  ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationErrorResponse error = new ValidationErrorResponse();
        error.getViolations().add(new Violation("deleteModelByName.name", "must not be blank"));

        String expected = objectMapper.writeValueAsString(error);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    private List<ModelBasicDTO> getModelBasicDTOs() {
        List<ModelBasicDTO> expected = new ArrayList<>();
        expected.add(new ModelBasicDTO(3L, "Grand"));
        expected.add(new ModelBasicDTO(2L, "Regal"));
        return expected;
    }

    private ModelDTO getModelDTO() {
        Set<CarWithoutModelDTO> cars = new HashSet<>();

        CarWithoutModelDTO carWithoutModelDTO1 = new CarWithoutModelDTO();
        carWithoutModelDTO1.setId(5L);
        cars.add(carWithoutModelDTO1);

        CarWithoutModelDTO carWithoutModelDTO2 = new CarWithoutModelDTO();
        carWithoutModelDTO2.setId(6L);
        cars.add(carWithoutModelDTO2);

        ModelDTO expected = new ModelDTO();
        expected.setId(3L);
        expected.setModel("Grand");
        expected.setCars(cars);

        return expected;
    }

    private ModelBasicDTO getModelBasicDTO(Long id, String name) {
        ModelBasicDTO modelBasicDTO = new ModelBasicDTO();
        modelBasicDTO.setId(id);
        modelBasicDTO.setModel(name);

        return modelBasicDTO;
    }
}
