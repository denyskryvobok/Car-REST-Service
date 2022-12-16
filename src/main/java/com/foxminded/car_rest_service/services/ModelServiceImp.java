package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.dao.ModelDAO;
import com.foxminded.car_rest_service.entities.Model;
import com.foxminded.car_rest_service.exceptions.custom.ModelAlreadyExistException;
import com.foxminded.car_rest_service.exceptions.custom.ModelNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.format;

@Slf4j
@Service
public class ModelServiceImp implements ModelService {

    @Autowired
    private ModelDAO modelDAO;

    @Override
    @Transactional(readOnly = true)
    public List<Model> getAllModels(Pageable pageable) {
        log.info("GetAllUniqueManufacturers started");

        List<Model> models = modelDAO.findAllPage(pageable);

        if (models.isEmpty()) {
            var exception = new ModelNotFoundException("No model was found");
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        }

        return models;
    }

    @Override
    @Transactional(readOnly = true)
    public Model getModelWithCarsByName(String name) {
        log.info("GetModelWithCarsByName started with name: {}", name);

        return modelDAO.findModelWithCarsByName(name).orElseThrow(() -> {
            var exception = new ModelNotFoundException("No model was found");
            log.error("Exception occurred during request processing: ", exception);
            return exception;
        });
    }

    @Override
    public Model createModel(Model model) {
        log.info("CreateModel started");

        modelDAO.findByName(model.getModel()).ifPresent(m -> {
            var exception = new ModelAlreadyExistException(format("Model with name(%s) already exist", m.getModel()));
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        });

        return modelDAO.save(model);
    }

    @Override
    public Model updateModel(Long id, Model inputModel) {
        log.info("UpdateModel started with id: {}, model: {}", id, inputModel);

        Model model = modelDAO.findById(id).orElseThrow(() -> {
            var exception = new ModelNotFoundException("No model was found");
            log.error("Exception occurred during request processing: ", exception);
            return exception;
        });

        model.setModel(inputModel.getModel());

        return modelDAO.save(model);
    }

    @Override
    public void deleteModelByName(String name) {
        log.info("DeleteModelByName started with name: {}", name);

        Model model = modelDAO.findModelWithCarsByName(name).orElseThrow(() -> {
            var exception = new ModelNotFoundException("No model was found");
            log.error("Exception occurred during request processing: ", exception);
            return exception;
        });

        modelDAO.delete(model);
    }
}
