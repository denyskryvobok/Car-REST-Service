package com.foxminded.car_rest_service.integration;

import org.junit.jupiter.api.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationTest extends IntegrationTestcontainersConfig {

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

    @Test
    void notGetEndpoint_shouldReturnStatus204_whenInputCredentialsAreCorrect() throws Exception {
        mockMvc.perform(delete("/api/v1/models/delete/name/{name}", "Regal")
                        .with(httpBasic("jamessmith", "password")))
                .andExpect(status().isNoContent());
    }
}
