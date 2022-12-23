package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.entities.Manufacturer;
import com.foxminded.car_rest_service.exceptions.custom.DataAlreadyExistException;
import com.foxminded.car_rest_service.exceptions.custom.DataNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void createManufacturer_shouldThrowDataAlreadyExistException_whenInputManufacturerExists() {
        assertThrows(DataAlreadyExistException.class,
                () -> manufacturerService.createManufacturer(getManufacturerBasicDTO(null, "Acura", 2017)));
    }

    @Test
    void updateManufacturer_shouldReturnManufacturerBasicDTO_whenManufacturerWasUpdated() {
        ManufacturerBasicDTO expected = getManufacturerBasicDTO(1L, "NEW", 2022);

        ManufacturerBasicDTO actual = manufacturerService.updateManufacturer(1L, expected);

        assertEquals(expected, actual);
    }

    @Test
    void updateManufacturer_shouldThrowDataNotFoundException_whenManufacturerNotFound() {
        ManufacturerBasicDTO input = getManufacturerBasicDTO(20L, "NEW", 2022);

        assertThrows(DataNotFoundException.class, () -> manufacturerService.updateManufacturer(20L, input));
    }

    @Test
    void deleteAllManufacturerByName_shouldDeleteAllManufacturersByName_whenManufacturersExist() {
        String inputName = "Acura";
        manufacturerService.deleteAllManufacturerByName(inputName);

        List<Manufacturer> actual = entityManager.createQuery(
                     "SELECT m " +
                        "FROM Manufacturer m " +
                        "WHERE m.manufacturer = :name ", Manufacturer.class).setParameter("name", inputName)
                                                        .getResultList();

        assertTrue(actual.isEmpty());
    }

    @Test
    void deleteAllManufacturerByName_shouldThrowDataNotFoundException_whenManufacturersNotExist() {
        assertThrows(DataNotFoundException.class, () -> manufacturerService.deleteAllManufacturerByName("INPUT_NAME"));
    }

    @Test
    void deleteManufacturerByNameAndYear_shouldDeleteManufacturer_whenManufactureExists() {
        manufacturerService.deleteManufacturerByNameAndYear("Acura", 2017);

        Manufacturer actual = entityManager.find(Manufacturer.class, 1L);

        assertNull(actual);
    }

    @Test
    void deleteManufacturerByNameAndYear_shouldThrowDataNotFoundException_whenManufactureNotExist() {
        assertThrows(DataNotFoundException.class, () -> manufacturerService.deleteManufacturerByNameAndYear("INPUT", 1500));
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
