package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.dao.ManufacturerDAO;
import com.foxminded.car_rest_service.entities.Manufacturer;
import com.foxminded.car_rest_service.exceptions.custom.ManufacturerAlreadyExistException;
import com.foxminded.car_rest_service.exceptions.custom.ManufacturerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static java.lang.String.format;

@Slf4j
@Service
public class ManufacturerServiceImp implements ManufacturerService {

    @Autowired
    private ManufacturerDAO manufacturerDAO;

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllUniqueManufacturers(Pageable pageable) {
        log.info("GetAllUniqueManufacturers started");

        List<String> manufacturers = manufacturerDAO.findAllUniqueManufacturers(pageable);

        if (manufacturers.isEmpty()) {
            var exception = new ManufacturerNotFoundException("No manufacturer was found");
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        }

        return manufacturers;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Manufacturer> getAllManufacturersByName(String name, Pageable pageable) {
        log.info("GetAllManufacturersByName started with name: {}", name);

        List<Manufacturer> manufacturers = manufacturerDAO.findAllManufacturersWithCarsByName(name, pageable);
        if (manufacturers.isEmpty()) {
            var exception = new ManufacturerNotFoundException("No manufacturer was found");
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        }

        return manufacturers;
    }

    @Override
    public Manufacturer createManufacturer(Manufacturer manufacturer) {
        log.info("CreateManufacturer started");

        manufacturerDAO.findByNameAndYear(manufacturer.getManufacturer(), manufacturer.getYear()).ifPresent(m -> {
            var exception =
                    new ManufacturerAlreadyExistException(
                            format("Manufacturer with name(%s) and year(%d) already exist", m.getManufacturer(), m.getYear()));
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        });

        return manufacturerDAO.save(manufacturer);
    }

    @Override
    public Manufacturer updateManufacturer(Long id, Manufacturer manufacturerRequest) {
        log.info("UpdateManufacturer started with id: {}, manufacturerRequest: {}", id, manufacturerRequest);

        Manufacturer manufacturer = manufacturerDAO.findById(id).orElseThrow(() -> {
            var exception = new ManufacturerNotFoundException("No manufacturer was found");
            log.error("Exception occurred during request processing: ", exception);
            return exception;
        });

        manufacturer.setYear(manufacturerRequest.getYear());
        manufacturer.setManufacturer(manufacturerRequest.getManufacturer());

        return manufacturerDAO.save(manufacturer);
    }

    @Override
    public void deleteAllManufacturerByName(String name) {
        log.info("DeleteManufacturer started with name: {}", name);

        Set<Manufacturer> manufacturers = manufacturerDAO.findAllByName(name);
        if (manufacturers.isEmpty()) {
            var exception = new ManufacturerNotFoundException("No manufacturer was found");
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        }

        manufacturerDAO.deleteAll(manufacturers);
    }

    @Override
    public void deleteManufacturerByNameAndYear(String name, Integer year) {
        log.info("DeleteManufacturerByNameAndYear started with name: {}, year: {}", name, year);

        Manufacturer manufacturer = manufacturerDAO.findByNameAndYearWithCars(name, year).orElseThrow(() -> {
            var exception = new ManufacturerNotFoundException("No manufacturer was found");
            log.error("Exception occurred during request processing: ", exception);
            return exception;
        });

        manufacturerDAO.delete(manufacturer);
    }
}
