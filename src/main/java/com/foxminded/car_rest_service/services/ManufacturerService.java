package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.entities.Manufacturer;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ManufacturerService {
    List<String> getAllUniqueManufacturers(Pageable pageable);

    Manufacturer createManufacturer(Manufacturer manufacturer);

    void deleteAllManufacturerByName(String name);

    void deleteManufacturerByNameAndYear(String name, Integer year);

    Manufacturer updateManufacturer(Long id, Manufacturer manufacturerRequest);

    List<Manufacturer> getAllManufacturersByName(String name, Pageable pageable);
}
