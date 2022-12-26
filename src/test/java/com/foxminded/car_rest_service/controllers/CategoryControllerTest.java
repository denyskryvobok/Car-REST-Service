package com.foxminded.car_rest_service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.car_rest_service.dao.AppUserDAO;
import com.foxminded.car_rest_service.exceptions.response.ResultModel;
import com.foxminded.car_rest_service.exceptions.response.ValidationErrorResponse;
import com.foxminded.car_rest_service.exceptions.response.Violation;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarWithoutCategoriesDTO;
import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryDTO;
import com.foxminded.car_rest_service.security.SecurityConfig;
import com.foxminded.car_rest_service.services.CategoryService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@MockBean(classes = AppUserDAO.class)
@WebMvcTest(controllers = CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllCategories_shouldReturnCategoryBasicDTOs_whenCategoriesExist() throws Exception {
        List<CategoryBasicDTO> categories = getCategories();
        when(categoryService.getAllCategories(any(Pageable.class))).thenReturn(categories);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setData(categories);

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    void getAllCategories_shouldReturnStatus404_whenCategoriesNotExist() throws Exception {
        when(categoryService.getAllCategories(any(Pageable.class))).thenReturn(List.of());

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Categories not found");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    void getCategoryWithCarsByName_shouldReturnCategoryDTO_whenCategoriesExist() throws Exception {
        CategoryDTO category = getCategoryWithCars();
        when(categoryService.getCategoryWithCarsByName(anyString())).thenReturn(category);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/categories/get/name/{name}", "Convertible")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setData(category);

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    void getCategoryWithCarsByName_shouldReturnStatus404_whenCategoriesNotExist() throws Exception {
        when(categoryService.getCategoryWithCarsByName(anyString())).thenReturn(null);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/categories/get/name/{name}", "Convertible")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Category with name(Convertible) not found");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void createCategory_shouldReturnCategoryBasicDTO_whenInputCategoryNotExists() throws Exception {
        CategoryBasicDTO category = getBasicCategory(1L, "name");

        when(categoryService.createCategory(any(CategoryBasicDTO.class)))
                .thenReturn(category);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getBasicCategory(null, "name"))))
                .andExpect(status().isCreated())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setData(category);

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void createCategory_shouldReturnStatus422_whenCategoryAlreadyExists() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getBasicCategory(null, "new"))))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Category with name(new) already exist");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void createCategory_shouldReturnStatus422_whenMethodArgumentNotValidExceptionWasThrown() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getBasicCategory(null, "  "))))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        ValidationErrorResponse error = new ValidationErrorResponse();
        error.getViolations().add(new Violation("category", "must not be blank"));

        String expected = objectMapper.writeValueAsString(error);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void updateCategory_shouldReturnStatus200AndReturnUpdatedCategory_whenCategoryWasUpdated() throws Exception {
        CategoryBasicDTO dto = getBasicCategory(1L, "new");

        when(categoryService.updateCategory(1L, dto)).thenReturn(dto);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/categories//update/id/{id}", 1)
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
    void updateCategory_shouldReturnStatus404_whenCategoryWasNotFound() throws Exception {
        CategoryBasicDTO dto = getBasicCategory(8L, "new");

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/categories/update/id/{id}", 8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Category with id(8) wasn't found");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void deleteCategoryByName_shouldReturnStatus204_whenCategoryWasDeleted() throws Exception {
        when(categoryService.deleteCategoryByName(anyString())).thenReturn(true);

        mockMvc.perform(delete("/api/v1/categories/delete/name/{name}", "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void deleteCategoryByName_shouldReturnStatus404_whenCategoryWasNotFound() throws Exception {
        when(categoryService.deleteCategoryByName(anyString())).thenReturn(false);

        MvcResult mvcResult = mockMvc.perform(delete("/api/v1/categories/delete/name/{name}", "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ResultModel resultModel = new ResultModel();
        resultModel.setMassage("Category with name(name) wasn't found");

        String expected = objectMapper.writeValueAsString(resultModel);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser
    void deleteCategoryByName_shouldReturnStatus400_whenConstraintViolationExceptionThrown() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/api/v1/categories/delete/name/{name}", "  ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationErrorResponse error = new ValidationErrorResponse();
        error.getViolations().add(new Violation("deleteCategoryByName.name", "must not be blank"));

        String expected = objectMapper.writeValueAsString(error);

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(expected, actual);
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
