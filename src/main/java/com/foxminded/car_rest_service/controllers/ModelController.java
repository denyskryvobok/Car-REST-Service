package com.foxminded.car_rest_service.controllers;

import com.foxminded.car_rest_service.exceptions.response.ResultModel;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelDTO;
import com.foxminded.car_rest_service.services.ModelService;
import com.foxminded.car_rest_service.utils.Mappings;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
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

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
@Validated
@RestController
@RequestMapping(value = Mappings.API_V1_MODELS, produces = APPLICATION_JSON_VALUE)
public class ModelController implements ModelOpenApi {

    @Autowired
    private ModelService modelService;

    @Override
    @GetMapping
    public ResponseEntity<ResultModel> getAllModels(@ParameterObject Pageable pageable) {
        log.info("GetAllModels started");

        ResultModel resultModel = new ResultModel();
        List<ModelBasicDTO> models = modelService.getAllModels(pageable);
        if (models.isEmpty()) {
            resultModel.setMassage("Models not found");
            return new ResponseEntity<>(resultModel, HttpStatus.NOT_FOUND);
        }
        resultModel.setData(models);

        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }

    @Override
    @GetMapping(Mappings.GET_MODEL_BY_NAME)
    public ResponseEntity<ResultModel> getModelWithCarsByName(@NotBlank @PathVariable(name = "name") String name) {
        log.info("GetModelWithCarsByName started with name: {}", name);

        ResultModel resultModel = new ResultModel();

        ModelDTO model = modelService.getModelWithCarsByName(name);
        if (model == null) {
            resultModel.setMassage(format("Model with name(%s) not found", name));
            return new ResponseEntity<>(resultModel, HttpStatus.NOT_FOUND);
        }
        resultModel.setData(model);

        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }

    @Override
    @PostMapping
    public ResponseEntity<ResultModel> createModel(@Valid @RequestBody ModelBasicDTO modelBasicDTO) {
        log.info("CreateModel started with input: {}", modelBasicDTO);

        ResultModel resultModel = new ResultModel();
        ModelBasicDTO model = modelService.createModel(modelBasicDTO);

        if (model == null) {
            resultModel.setMassage(format("Model with name(%s) already exists", modelBasicDTO.getModel()));

            return new ResponseEntity<>(resultModel, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        resultModel.setData(model);

        return new ResponseEntity<>(resultModel, HttpStatus.CREATED);
    }

    @Override
    @PutMapping(Mappings.UPDATE_MODEL_BY_ID)
    public ResponseEntity<ResultModel> updateModel(@PathVariable("id") Long id,
                                                   @Valid @RequestBody ModelBasicDTO modelBasicDTO) {
        log.info("UpdateModel started with id: {}, modelBasicDTO: {}", id, modelBasicDTO);

        ResultModel resultModel = new ResultModel();
        ModelBasicDTO model = modelService.updateModel(id, modelBasicDTO);

        if (model == null) {
            resultModel.setMassage(format("Model with id(%d) wasn't found", id));

            return new ResponseEntity<>(resultModel, HttpStatus.NOT_FOUND);
        }

        resultModel.setData(model);

        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }

    @Override
    @DeleteMapping(Mappings.DELETE_MODEL_BY_NAME)
    public ResponseEntity<?> deleteModelByName(@NotBlank @PathVariable(name = "name") String name) {
        log.info("DeleteModelByName started with name: {}", name);

        boolean isDeleted = modelService.deleteModelByName(name);

        if (!isDeleted) {
            ResultModel resultModel = new ResultModel();
            resultModel.setMassage(format("Model with name(%s) wasn't found", name));
            return new ResponseEntity<>(resultModel, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
