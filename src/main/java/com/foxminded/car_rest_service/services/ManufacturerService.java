package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ManufacturerService {
    List<String> getAllUniqueManufacturers(Pageable pageable);

    ManufacturerBasicDTO createManufacturer(ManufacturerBasicDTO manufacturerBasicDTO);

    void deleteAllManufacturerByName(String name);

    void deleteManufacturerByNameAndYear(String name, Integer year);

    ManufacturerBasicDTO updateManufacturer(Long id, ManufacturerBasicDTO ManufacturerBasicDTO);

    List<ManufacturerDTO> getAllManufacturersByName(String name, Pageable pageable);
}
