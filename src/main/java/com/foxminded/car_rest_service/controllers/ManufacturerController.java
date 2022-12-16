package com.foxminded.car_rest_service.controllers;

import com.foxminded.car_rest_service.entities.Manufacturer;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerDTO;
import com.foxminded.car_rest_service.mapstruct.mapper.ManufacturerMapper;
import com.foxminded.car_rest_service.services.ManufacturerService;
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
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/manufacturers")
public class ManufacturerController {

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private ManufacturerMapper mapper;

    @GetMapping
    public List<String> getAllUniqueManufacturers(Pageable pageable) {
        log.info("GetAllUniqueManufacturers started");

        return manufacturerService.getAllUniqueManufacturers(pageable);
    }

    @GetMapping("{name}")
    public List<ManufacturerDTO> getAllManufacturersByName(@NotBlank @PathVariable(name = "name") String name,
                                                           Pageable pageable) {
        log.info("GetAllManufacturersByName started");

        return manufacturerService.getAllManufacturersByName(name, pageable).stream()
                .map(manufacturer -> mapper.manufacturerToManufacturerDTO(manufacturer))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<ManufacturerBasicDTO> createManufacturer(@Valid @RequestBody ManufacturerBasicDTO manufacturerDTO) {
        log.info("CreateManufacturer started with input: {}", manufacturerDTO);

        Manufacturer manufacturerRequest = mapper.manufacturerBasicDTOToManufacturer(manufacturerDTO);

        Manufacturer manufacturer = manufacturerService.createManufacturer(manufacturerRequest);

        var manufacturerResponse = mapper.manufacturerToManufacturerBasicDTO(manufacturer);


        return ResponseEntity.status(HttpStatus.CREATED).body(manufacturerResponse);
    }

    @PutMapping("{id}")
    public ResponseEntity<ManufacturerBasicDTO> updateManufacturer(@PathVariable("id") Long id,
                                                                   @Valid @RequestBody ManufacturerBasicDTO manufacturerDTO) {
        log.info("UpdateManufacturer started with id: {}, manufacturerDTO: {}", id, manufacturerDTO);

        Manufacturer manufacturerRequest = mapper.manufacturerBasicDTOToManufacturer(manufacturerDTO);

        Manufacturer manufacturer = manufacturerService.updateManufacturer(id, manufacturerRequest);

        var manufacturerResponse = mapper.manufacturerToManufacturerBasicDTO(manufacturer);

        return ResponseEntity.ok().body(manufacturerResponse);
    }

    @DeleteMapping("/{name}/year/{year}")
    public ResponseEntity<?> deleteManufacturerByNameAndYear(@NotBlank @PathVariable(name = "name") String name,
                                                             @PathVariable(name = "year") Integer year) {
        log.info("DeleteManufacturerByNameAndYear started with name: {}, year: {}", name, year);

        manufacturerService.deleteManufacturerByNameAndYear(name, year);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{name}")
    public ResponseEntity<?> deleteAllManufacturerByName(@NotBlank @PathVariable(name = "name") String name) {
        log.info("DeleteManufacturer started with name: {}", name);

        manufacturerService.deleteAllManufacturerByName(name);

        return ResponseEntity.noContent().build();
    }
}
