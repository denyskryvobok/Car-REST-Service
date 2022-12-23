package com.foxminded.car_rest_service.controllers;

import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerDTO;
import com.foxminded.car_rest_service.services.ManufacturerService;
import com.foxminded.car_rest_service.utils.Mappings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(Mappings.API_V1_MANUFACTURERS)
public class ManufacturerController {

    @Autowired
    private ManufacturerService manufacturerService;

    @GetMapping
    public ResponseEntity<List<String>> getAllUniqueManufacturers(Pageable pageable) {
        log.info("GetAllUniqueManufacturers started");

        return new ResponseEntity<>(manufacturerService.getAllUniqueManufacturers(pageable), HttpStatus.OK);
    }

    @GetMapping(Mappings.GET_MANUFACTURER_BY_NAME)
    public ResponseEntity<List<ManufacturerDTO>> getAllManufacturersByName(@NotBlank @PathVariable("name") String name,
                                                                           Pageable pageable) {
        log.info("GetAllManufacturersByName started with name: {}", name);

        return new ResponseEntity<>(manufacturerService.getAllManufacturersByName(name, pageable), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ManufacturerBasicDTO> createManufacturer(@Valid @RequestBody ManufacturerBasicDTO manufacturerDTO) {
        log.info("CreateManufacturer started with input: {}", manufacturerDTO);

        return new ResponseEntity<>(manufacturerService.createManufacturer(manufacturerDTO), HttpStatus.CREATED);
    }

    @PutMapping(Mappings.UPDATE_MANUFACTURER_BY_ID)
    public ResponseEntity<ManufacturerBasicDTO> updateManufacturer(@PathVariable("id") Long id,
                                                                   @Valid @RequestBody ManufacturerBasicDTO manufacturerBasicDTO) {
        log.info("UpdateManufacturer started with id: {}, manufacturerBasicDTO: {}", id, manufacturerBasicDTO);

        return new ResponseEntity<>(manufacturerService.updateManufacturer(id, manufacturerBasicDTO), HttpStatus.OK);
    }

    @DeleteMapping(Mappings.DELETE_MANUFACTURER_BY_NAME_AND_YEAR)
    public ResponseEntity<?> deleteManufacturerByNameAndYear(@NotBlank @PathVariable(name = "name") String name,
                                                             @PathVariable(name = "year") Integer year) {
        log.info("DeleteManufacturerByNameAndYear started with name: {}, year: {}", name, year);

        manufacturerService.deleteManufacturerByNameAndYear(name, year);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(Mappings.DELETE_MANUFACTURER_BY_NAME)
    public ResponseEntity<?> deleteAllManufacturerByName(@NotBlank @PathVariable(name = "name") String name) {
        log.info("DeleteManufacturerByName started with name: {}", name);

        manufacturerService.deleteAllManufacturerByName(name);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
