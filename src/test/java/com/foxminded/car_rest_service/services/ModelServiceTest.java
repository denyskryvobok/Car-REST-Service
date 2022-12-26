package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.entities.Model;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarWithoutModelDTO;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModelServiceTest extends TestcontainersConfig {

    @Autowired
    private ModelService modelService;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void getAllModels_shouldReturnListOfPageableModelBasicDTO_whenPageableIsProvided() {
        List<ModelBasicDTO> expected = getModelBasicDTOs();

        List<ModelBasicDTO> actual =
                modelService.getAllModels(PageRequest.of(0, 2, Sort.by("model").ascending()));

        assertEquals(expected, actual);
    }

    @Test
    void getModelWithCarsByName_shouldReturnModelDTOWithCars_whenInputNameExists() {
        ModelDTO expected = getModelDTO();

        ModelDTO actual = modelService.getModelWithCarsByName("Grand");

        assertAll(() -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getCars(), actual.getCars()));
    }

    @Test
    void getModelWithCarsByName_shouldReturnEmptyModelDTOWithCars_whenInputNameNotExist() {
        ModelDTO actual = modelService.getModelWithCarsByName("car");

        assertAll(
                () -> assertNull(actual.getId()),
                () -> assertNull(actual.getModel()),
                () -> assertTrue(actual.getCars().isEmpty()));
    }

    @Test
    void createModel_shouldReturnCreatedModelWithId_whenModelNotExist() {
        ModelBasicDTO expected = getModelBasicDTO(4L, "Car");

        ModelBasicDTO actual = modelService.createModel(getModelBasicDTO(null, "Car"));

        assertEquals(expected, actual);
    }

    @Test
    void createModel_shouldReturnNull_whenModelExist() {
        ModelBasicDTO actual = modelService.createModel(getModelBasicDTO(null, "Grand"));
        System.out.println(actual);
        assertNull(actual);

    }

    @Test
    void updateModel_shouldReturnModelBasicDTO_whenModelByIdExists() {
        ModelBasicDTO expected = getModelBasicDTO(3L, "NEW_NAME");

        ModelBasicDTO actual = modelService.updateModel(3L, expected);

        assertEquals(expected, actual);
    }

    @Test
    void updateModel_shouldReturnNull_whenModelByIdNotExist() {
        ModelBasicDTO actual = modelService.updateModel(4L, getModelBasicDTO(4L, "NEW_NAME"));
        assertNull(actual);
    }

    @Test
    void deleteModelByName_shouldDeleteModelAndReturnTrue_whenModelWithInputNameExists() {
        boolean isDeleted = modelService.deleteModelByName("Grand");

        Model actual = entityManager.find(Model.class, 3L);

        assertTrue(isDeleted);
        assertNull(actual);
    }

    @Test
    void deleteModelByName_shouldReturnFalse_whenModelWithInputNameNotExist() {
        boolean actual = modelService.deleteModelByName("Car");
        assertFalse(actual);
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
