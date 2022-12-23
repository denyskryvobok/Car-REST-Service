package com.foxminded.car_rest_service.controllers;

import com.foxminded.car_rest_service.dao.AppUserDAO;
import com.foxminded.car_rest_service.security.SecurityConfig;
import com.foxminded.car_rest_service.services.ModelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@MockBean(classes = {AppUserDAO.class, ModelService.class})
@WebMvcTest(controllers = ModelController.class)
class AuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getEndpoint_shouldReturnStatus200_whenUserNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/models/"))
                .andExpect(status().isOk());
    }


    @Test
    void notGetEndpoint_shouldReturnStatus401_whenUserNotAuthenticated() throws Exception {
        mockMvc.perform(delete("/api/v1/models/delete/name/{name}", "name"))
                .andExpect(status().isUnauthorized());
    }
}
