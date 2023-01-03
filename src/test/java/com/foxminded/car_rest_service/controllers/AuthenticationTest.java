package com.foxminded.car_rest_service.controllers;

import com.foxminded.car_rest_service.mapstruct.dto.model.ModelBasicDTO;
import com.foxminded.car_rest_service.services.ModelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Import(SecurityConfigTest.class)
@WebMvcTest(controllers = ModelController.class)
class AuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ModelService modelService;

    @Test
    @WithMockUser(roles = "USER")
    void getEndpoint_shouldReturnStatus200_whenUserHasUserRole() throws Exception {
        when(modelService.getAllModels(any(Pageable.class))).thenReturn(getModelBasicDTOs());
        mockMvc.perform(get("/api/v1/models/"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(roles = {"USER", "STAFF"})
    void deleteEndpoint_shouldReturnStatus401_whenUserNotHaveAdminRole() throws Exception {
        mockMvc.perform(delete("/api/v1/models/name/{name}", "name"))
                .andExpect(status().isForbidden());
    }

    private List<ModelBasicDTO> getModelBasicDTOs() {
        List<ModelBasicDTO> expected = new ArrayList<>();
        expected.add(new ModelBasicDTO(3L, "Grand"));
        expected.add(new ModelBasicDTO(2L, "Regal"));
        return expected;
    }
}
