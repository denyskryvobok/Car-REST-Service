package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.entities.Manufacturer;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ManufacturerServiceTest extends TestcontainersConfig {

    @Autowired
    private ManufacturerService manufacturerService;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void getAllUniqueManufacturers_shouldReturnListOfManufacturersName_whenManufacturersExist() {
        PageRequest pageable = PageRequest.of(0, 2, Sort.by("manufacturer").ascending());

        List<String> expected = getAllNamesOfManufacturers();

        List<String> actual = manufacturerService.getAllUniqueManufacturers(pageable);

        assertEquals(expected, actual);
    }

    @Test
    void getAllManufacturersByName_shouldReturnListOfManufacturerDTOs_whenManufacturersExist() {
        PageRequest pageable = PageRequest.of(0, 3, Sort.by("manufacturer").ascending());

        List<ManufacturerDTO> expected = getAllManufacturers();

        List<ManufacturerDTO> actual = manufacturerService.getAllManufacturersByName("Acura", pageable);

        assertEquals(expected, actual);
    }

    @Test
    void createManufacturer_shouldReturnManufacturerBasicDTOWithId_whenInputManufacturerNotExist() {
        ManufacturerBasicDTO expected = getManufacturerBasicDTO(7L, "NEW_MANUFACTURER", 2030);

        ManufacturerBasicDTO actual = manufacturerService.createManufacturer(getManufacturerBasicDTO(null, "NEW_MANUFACTURER", 2030));

        assertEquals(expected, actual);
    }

    @Test
    void createManufacturer_shouldReturnNull_whenInputManufacturerExists() {
        ManufacturerBasicDTO actual = manufacturerService.createManufacturer(getManufacturerBasicDTO(null, "Acura", 2017));
        assertNull(actual);
    }

    @Test
    void updateManufacturer_shouldReturnManufacturerBasicDTO_whenManufacturerWasUpdated() {
        ManufacturerBasicDTO expected = getManufacturerBasicDTO(1L, "NEW", 2022);

        ManufacturerBasicDTO actual = manufacturerService.updateManufacturer(1L, expected);

        assertEquals(expected, actual);
    }

    @Test
    void updateManufacturer_shouldReturnNull_whenManufacturerNotFound() {
        ManufacturerBasicDTO input = getManufacturerBasicDTO(20L, "NEW", 2022);
        ManufacturerBasicDTO actual = manufacturerService.updateManufacturer(20L, input);
        assertNull(actual);
    }

    @Test
    void deleteAllManufacturerByName_shouldDeleteAllManufacturersByNameAndReturnTrue_whenManufacturersExist() {
        String inputName = "Acura";
        boolean isDeleted = manufacturerService.deleteAllManufacturerByName(inputName);

        List<Manufacturer> actual = entityManager.createQuery(
                        "SELECT m " +
                                "FROM Manufacturer m " +
                                "WHERE m.manufacturer = :name ", Manufacturer.class).setParameter("name", inputName)
                .getResultList();

        assertTrue(actual.isEmpty());
        assertTrue(isDeleted);
    }

    @Test
    void deleteAllManufacturerByName_shouldReturnFalse_whenManufacturersNotExist() {
        boolean isDeleted = manufacturerService.deleteAllManufacturerByName("INPUT_NAME");
        assertFalse(isDeleted);
    }

    @Test
    void deleteManufacturerByNameAndYear_shouldDeleteManufacturerAndReturnTrue_whenManufactureExists() {
        boolean isDeleted = manufacturerService.deleteManufacturerByNameAndYear("Acura", 2017);

        Manufacturer actual = entityManager.find(Manufacturer.class, 1L);

        assertNull(actual);
        assertTrue(isDeleted);
    }

    @Test
    void deleteManufacturerByNameAndYear_shouldReturnFalse_whenManufactureNotExist() {
        boolean isDeleted = manufacturerService.deleteManufacturerByNameAndYear("INPUT", 1500);
        assertFalse(isDeleted);
    }

    private List<String> getAllNamesOfManufacturers() {
        return List.of("Acura", "Aston Martin");
    }

    private List<ManufacturerDTO> getAllManufacturers() {
        ManufacturerDTO m1 = new ManufacturerDTO();
        m1.setId(1L);
        ManufacturerDTO m2 = new ManufacturerDTO();
        m2.setId(2L);
        ManufacturerDTO m3 = new ManufacturerDTO();
        m3.setId(3L);

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
