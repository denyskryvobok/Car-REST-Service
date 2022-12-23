package com.foxminded.car_rest_service.controllers;

import com.foxminded.car_rest_service.mapstruct.dto.model.ModelBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelDTO;
import com.foxminded.car_rest_service.services.ModelService;
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
@RequestMapping(Mappings.API_V1_MODELS)
public class ModelController {

    @Autowired
    private ModelService modelService;

    @GetMapping
    public ResponseEntity<List<ModelBasicDTO>> getAllModels(Pageable pageable) {
        log.info("GetAllModels started");

        return new ResponseEntity<>(modelService.getAllModels(pageable), HttpStatus.OK);
    }

    @GetMapping(Mappings.GET_MODEL_BY_NAME)
    public ResponseEntity<ModelDTO> getModelWithCarsByName(@NotBlank @PathVariable(name = "name") String name) {
        log.info("GetModelWithCarsByName started with name: {}", name);

        return new ResponseEntity<>(modelService.getModelWithCarsByName(name), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ModelBasicDTO> createModel(@Valid @RequestBody ModelBasicDTO modelBasicDTO) {
        log.info("CreateModel started with input: {}", modelBasicDTO);

        return new ResponseEntity<>(modelService.createModel(modelBasicDTO), HttpStatus.CREATED);
    }

    @PutMapping(Mappings.UPDATE_MODEL_BY_ID)
    public ResponseEntity<ModelBasicDTO> updateModel(@PathVariable("id") Long id,
                                                     @Valid @RequestBody ModelBasicDTO modelBasicDTO) {
        log.info("UpdateModel started with id: {}, modelBasicDTO: {}", id, modelBasicDTO);

        return new ResponseEntity<>(modelService.updateModel(id, modelBasicDTO), HttpStatus.OK);
    }

    @DeleteMapping(Mappings.DELETE_MODEL_BY_NAME)
    public ResponseEntity<?> deleteModelByName(@NotBlank @PathVariable(name = "name") String name) {
        log.info("DeleteModelByName started with name: {}", name);

        modelService.deleteModelByName(name);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
