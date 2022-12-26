package com.foxminded.car_rest_service.controllers;

import com.foxminded.car_rest_service.exceptions.response.ResultModel;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerBasicDTO;
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

import static java.lang.String.format;

@Slf4j
@Validated
@RestController
@RequestMapping(Mappings.API_V1_MANUFACTURERS)
public class ManufacturerController {

    @Autowired
    private ManufacturerService manufacturerService;

    @GetMapping
    public ResponseEntity<ResultModel> getAllUniqueManufacturers(Pageable pageable) {
        log.info("GetAllUniqueManufacturers started");

        ResultModel resultModel = new ResultModel();
        resultModel.setData(manufacturerService.getAllUniqueManufacturers(pageable));

        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }

    @GetMapping(Mappings.GET_MANUFACTURER_BY_NAME)
    public ResponseEntity<ResultModel> getAllManufacturersByName(@NotBlank @PathVariable("name") String name,
                                                                 Pageable pageable) {
        log.info("GetAllManufacturersByName started with name: {}", name);

        ResultModel resultModel = new ResultModel();
        resultModel.setData(manufacturerService.getAllManufacturersByName(name, pageable));

        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResultModel> createManufacturer(@Valid @RequestBody ManufacturerBasicDTO manufacturerDTO) {
        log.info("CreateManufacturer started with input: {}", manufacturerDTO);

        ResultModel resultModel = new ResultModel();

        ManufacturerBasicDTO manufacturer = manufacturerService.createManufacturer(manufacturerDTO);

        if (manufacturer == null) {
            resultModel.setMassage(format("Manufacturer with name(%s) and year(%d) already exist",
                    manufacturerDTO.getManufacturer(), manufacturerDTO.getYear()));

            return new ResponseEntity<>(resultModel, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        resultModel.setData(manufacturer);

        return new ResponseEntity<>(resultModel, HttpStatus.CREATED);
    }

    @PutMapping(Mappings.UPDATE_MANUFACTURER_BY_ID)
    public ResponseEntity<ResultModel> updateManufacturer(@PathVariable("id") Long id,
                                                          @Valid @RequestBody ManufacturerBasicDTO manufacturerBasicDTO) {
        log.info("UpdateManufacturer started with id: {}, manufacturerBasicDTO: {}", id, manufacturerBasicDTO);

        ResultModel resultModel = new ResultModel();

        ManufacturerBasicDTO manufacturer = manufacturerService.updateManufacturer(id, manufacturerBasicDTO);

        if (manufacturer == null) {
            resultModel.setMassage(format("Manufacturer with id(%d) wasn't found", id));

            return new ResponseEntity<>(resultModel, HttpStatus.NOT_FOUND);
        }

        resultModel.setData(manufacturer);

        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }

    @DeleteMapping(Mappings.DELETE_MANUFACTURER_BY_NAME_AND_YEAR)
    public ResponseEntity<?> deleteManufacturerByNameAndYear(@NotBlank @PathVariable(name = "name") String name,
                                                             @PathVariable(name = "year") Integer year) {
        log.info("DeleteManufacturerByNameAndYear started with name: {}, year: {}", name, year);

        boolean isDeleted = manufacturerService.deleteManufacturerByNameAndYear(name, year);

        if (!isDeleted) {
            ResultModel resultModel = new ResultModel();
            resultModel.setMassage(format("Manufacturers with name(%s) and year(%d) wasn't found", name, year));
            return new ResponseEntity<>(resultModel, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(Mappings.DELETE_MANUFACTURER_BY_NAME)
    public ResponseEntity<?> deleteAllManufacturerByName(@NotBlank @PathVariable(name = "name") String name) {
        log.info("DeleteManufacturerByName started with name: {}", name);

        boolean isDeleted = manufacturerService.deleteAllManufacturerByName(name);

        if (!isDeleted) {
            ResultModel resultModel = new ResultModel();
            resultModel.setMassage(format("Manufacturers with name(%s) weren't found", name));
            return new ResponseEntity<>(resultModel, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
