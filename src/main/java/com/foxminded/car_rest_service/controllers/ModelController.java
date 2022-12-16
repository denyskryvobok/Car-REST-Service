package com.foxminded.car_rest_service.controllers;

import com.foxminded.car_rest_service.entities.Model;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelDTO;
import com.foxminded.car_rest_service.mapstruct.mapper.ModelMapper;
import com.foxminded.car_rest_service.services.ModelService;
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

import static java.util.stream.Collectors.toList;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/models")
public class ModelController {

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping
    public List<ModelBasicDTO> getAllModels(Pageable pageable) {
        log.info("GetAllModels started");

        return modelService.getAllModels(pageable).stream()
                .map(model -> mapper.modelToModelBasicDTO(model))
                .collect(toList());
    }

    @GetMapping("{name}")
    public ResponseEntity<ModelDTO> getModelWithCarsByName(@NotBlank @PathVariable(name = "name") String name) {
        log.info("GetModelWithCarsByName started");

        return ResponseEntity.ok().body(mapper.modelToModelDTO(modelService.getModelWithCarsByName(name)));
    }

    @PostMapping
    public ResponseEntity<ModelBasicDTO> createModel(@Valid @RequestBody ModelBasicDTO modelBasicDTO) {
        log.info("CreateModel started with input: {}", modelBasicDTO);

        Model model = modelService.createModel(mapper.modelBasicDTOToModel(modelBasicDTO));

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.modelToModelBasicDTO(model));
    }

    @PutMapping("{id}")
    public ResponseEntity<ModelBasicDTO> updateModel(@PathVariable("id") Long id,
                                                     @Valid @RequestBody ModelBasicDTO modelBasicDTO) {
        log.info("UpdateModel started with id: {}, modelBasicDTO: {}", id, modelBasicDTO);

        Model model = modelService.updateModel(id, mapper.modelBasicDTOToModel(modelBasicDTO));

        return ResponseEntity.ok().body(mapper.modelToModelBasicDTO(model));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteModelByName(@NotBlank @PathVariable(name = "name") String name) {
        log.info("DeleteModelByName started with name: {}", name);

        modelService.deleteModelByName(name);

        return ResponseEntity.noContent().build();
    }
}
