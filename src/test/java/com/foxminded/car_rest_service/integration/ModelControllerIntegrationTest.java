package com.foxminded.car_rest_service.integration;

import com.foxminded.car_rest_service.exceptions.response.ResultModel;
import com.foxminded.car_rest_service.exceptions.response.ValidationErrorResponse;
import com.foxminded.car_rest_service.exceptions.response.Violation;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarWithoutModelDTO;
import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ModelControllerIntegrationTest extends IntegrationTestcontainersConfig {

    @Test
    void getAllModels_shouldReturnStatus200WithJsonResponseBody_whenModelsExists() throws Exception {
        List<ModelBasicDTO> modelBasicDTOs = getModelBasicDTOs();

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
    void getModelWithCarsByName_shouldReturnStatus200WithJsonResponseBody_whenModelsExists() throws Exception {
        ModelDTO modelDTO = getModelDTO();

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/models/get/name/{name}", "Grand")
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
    void getModelWithCarsByName_shouldReturnStatus404_whenModelNotExists() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/models/get/name/{name}", "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Model with name(name) not found");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    void getModelWithCarsByName_shouldReturnStatus400_whenModelsExists() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/models/get/name/{name}", "  ")
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
    @WithUserDetails("jamessmith")
    void createModel_shouldReturnStatus201WithJsonResponseBody_whenConstraintViolationException() throws Exception {
        ModelBasicDTO dto = getModelBasicDTO(4L, "NEW");

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
    @WithUserDetails("jamessmith")
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
    @WithUserDetails("jamessmith")
    void createModel_shouldReturnStatus422_whenModelAlreadyExist() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getModelBasicDTO(null, "Regal"))))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Model with name(Regal) already exists");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails("jamessmith")
    void updateModel_shouldReturnStatus200AndReturnUpdatedModel_whenModelWasUpdated() throws Exception {
        ModelBasicDTO dto = getModelBasicDTO(1L, "new");

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/models//update/id/{id}", 1)
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
    void updateModel_shouldReturnStatus404_whenModelWasNotFound() throws Exception {
        ModelBasicDTO dto = getModelBasicDTO(8L, "new");

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/models/update/id/{id}", 8)
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
    @WithUserDetails("jamessmith")
    void deleteModelByName_shouldReturnStatus204_whenModelWasDeleted() throws Exception {
        mockMvc.perform(delete("/api/v1/models/delete/name/{name}", "Regal")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails("jamessmith")
    void deleteModelByName_shouldReturnStatus404_whenModelWasNotFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/api/v1/models/delete/name/{name}", "name")
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
    @WithUserDetails("jamessmith")
    void deleteModelByName_shouldReturnStatus400_whenConstraintViolationExceptionThrown() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/api/v1/models/delete/name/{name}", "  ")
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
        expected.add(new ModelBasicDTO(1L, "Touareg 2"));
        expected.add(new ModelBasicDTO(2L, "Regal"));
        expected.add(new ModelBasicDTO(3L, "Grand"));
        return expected;
    }

    private ModelDTO getModelDTO() {
        Set<CarWithoutModelDTO> cars = new HashSet<>();

        CarWithoutModelDTO carWithoutModelDTO1 = new CarWithoutModelDTO();
        carWithoutModelDTO1.setManufacturer(new ManufacturerBasicDTO(4L, "Aston Martin", 2010));
        carWithoutModelDTO1.setId(5L);
        carWithoutModelDTO1.setCarCategories(Set.of(new CategoryBasicDTO(2L, "Convertible")));
        cars.add(carWithoutModelDTO1);

        CarWithoutModelDTO carWithoutModelDTO2 = new CarWithoutModelDTO();
        carWithoutModelDTO2.setManufacturer(new ManufacturerBasicDTO(5L, "Aston Martin", 2012));
        carWithoutModelDTO2.setId(6L);
        carWithoutModelDTO2.setCarCategories(Set.of(new CategoryBasicDTO(3L, "Wagon")));
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
