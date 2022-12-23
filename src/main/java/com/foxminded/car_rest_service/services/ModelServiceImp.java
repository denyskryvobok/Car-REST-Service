package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.dao.ModelDAO;
import com.foxminded.car_rest_service.entities.Model;
import com.foxminded.car_rest_service.exceptions.custom.DataAlreadyExistException;
import com.foxminded.car_rest_service.exceptions.custom.DataNotFoundException;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelDTO;
import com.foxminded.car_rest_service.mapstruct.mapper.ModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class ModelServiceImp implements ModelService {

    @Autowired
    private ModelDAO modelDAO;

    @Autowired
    private ModelMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ModelBasicDTO> getAllModels(Pageable pageable) {
        log.info("GetAllUniqueManufacturers started");

        List<Model> models = modelDAO.findAllPage(pageable);

        return models.stream()
                     .map(model -> mapper.modelToModelBasicDTO(model))
                     .collect(toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ModelDTO getModelWithCarsByName(String name) {
        log.info("GetModelWithCarsByName started with name: {}", name);

        Model model = modelDAO.findModelWithCarsByName(name).orElseGet(() -> {
            log.info(format("Model with name(%s) wasn't found", name));
            return new Model();
        });

        return mapper.modelToModelDTO(model);
    }

    @Override
    public ModelBasicDTO createModel(ModelBasicDTO modelBasicDTO) {
        log.info("CreateModel started with input: {}", modelBasicDTO);

        modelDAO.findByName(modelBasicDTO.getModel()).ifPresent(m -> {
            var exception = new DataAlreadyExistException(format("Model with name(%s) already exists", m.getModel()));
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        });

        return mapper.modelToModelBasicDTO(modelDAO.save(mapper.modelBasicDTOToModel(modelBasicDTO)));
    }

    @Override
    public ModelBasicDTO updateModel(Long id, ModelBasicDTO modelBasicDTO) {
        log.info("UpdateModel started with id: {}, model: {}", id, modelBasicDTO);

        Model model = modelDAO.findById(id).orElseThrow(() -> {
            var exception = new DataNotFoundException(format("Model with id(%d) wasn't found", id));
            log.error("Exception occurred during request processing: ", exception);
            return exception;
        });

        model.setModel(modelBasicDTO.getModel());

        return mapper.modelToModelBasicDTO(modelDAO.save(model));
    }

    @Override
    public void deleteModelByName(String name) {
        log.info("DeleteModelByName started with name: {}", name);

        Model model = modelDAO.findModelWithCarsByName(name).orElseThrow(() -> {
            var exception = new DataNotFoundException(format("Model with name(%s) wasn't found", name));
            log.error("Exception occurred during request processing: ", exception);
            return exception;
        });

        modelDAO.delete(model);
    }
}
