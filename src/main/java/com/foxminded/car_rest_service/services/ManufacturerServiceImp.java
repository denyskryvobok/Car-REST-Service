package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.dao.ManufacturerDAO;
import com.foxminded.car_rest_service.entities.Manufacturer;
import com.foxminded.car_rest_service.exceptions.custom.DataAlreadyExistException;
import com.foxminded.car_rest_service.exceptions.custom.DataNotFoundException;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerDTO;
import com.foxminded.car_rest_service.mapstruct.mapper.ManufacturerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Service
public class ManufacturerServiceImp implements ManufacturerService {

    @Autowired
    private ManufacturerDAO manufacturerDAO;

    @Autowired
    private ManufacturerMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllUniqueManufacturers(Pageable pageable) {
        log.info("GetAllUniqueManufacturers started");

        return manufacturerDAO.findAllUniqueManufacturers(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManufacturerDTO> getAllManufacturersByName(String name, Pageable pageable) {
        log.info("GetAllManufacturersByName started with name: {}", name);

        List<Manufacturer> manufacturers = manufacturerDAO.findAllManufacturersWithCarsByName(name, pageable);

        return manufacturers.stream()
                            .map(manufacturer -> mapper.manufacturerToManufacturerDTO(manufacturer))
                            .collect(Collectors.toList());
    }

    @Override
    public ManufacturerBasicDTO createManufacturer(ManufacturerBasicDTO manufacturerBasicDTO) {
        log.info("CreateManufacturer started");

        manufacturerDAO.findByNameAndYear(manufacturerBasicDTO.getManufacturer(), manufacturerBasicDTO.getYear()).ifPresent(m -> {
            var exception =
                    new DataAlreadyExistException(
                            format("Manufacturer with name(%s) and year(%d) already exist", m.getManufacturer(), m.getYear()));
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        });

        Manufacturer manufacturer = mapper.manufacturerBasicDTOToManufacturer(manufacturerBasicDTO);

        return mapper.manufacturerToManufacturerBasicDTO(manufacturerDAO.save(manufacturer));
    }

    @Override
    public ManufacturerBasicDTO updateManufacturer(Long id, ManufacturerBasicDTO manufacturerBasicDTO) {
        log.info("UpdateManufacturer started with id: {}, manufacturerBasicDTO: {}", id, manufacturerBasicDTO);

        Manufacturer manufacturer = manufacturerDAO.findById(id).orElseThrow(() -> {
            var exception = new DataNotFoundException(format("Manufacturer with id(%d) wasn't found", id));
            log.error("Exception occurred during request processing: ", exception);
            return exception;
        });

        manufacturer.setYear(manufacturerBasicDTO.getYear());
        manufacturer.setManufacturer(manufacturerBasicDTO.getManufacturer());

        return mapper.manufacturerToManufacturerBasicDTO(manufacturerDAO.save(manufacturer));
    }

    @Override
    public void deleteAllManufacturerByName(String name) {
        log.info("DeleteManufacturer started with name: {}", name);

        Set<Manufacturer> manufacturers = manufacturerDAO.findAllByName(name);
        if (manufacturers.isEmpty()) {
            var exception = new DataNotFoundException(format("Manufacturers with name(%s) weren't found", name));
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        }

        manufacturerDAO.deleteAll(manufacturers);
    }

    @Override
    public void deleteManufacturerByNameAndYear(String name, Integer year) {
        log.info("DeleteManufacturerByNameAndYear started with name: {}, year: {}", name, year);

        Manufacturer manufacturer = manufacturerDAO.findByNameAndYearWithCars(name, year).orElseThrow(() -> {
            var exception = new DataNotFoundException(format("Manufacturers with name(%s) and year(%d) wasn't found", name, year));
            log.error("Exception occurred during request processing: ", exception);
            return exception;
        });

        manufacturerDAO.delete(manufacturer);
    }
}
