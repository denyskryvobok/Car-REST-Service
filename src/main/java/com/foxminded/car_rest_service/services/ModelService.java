package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.entities.Model;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ModelService {
    List<Model> getAllModels(Pageable pageable);

    Model getModelWithCarsByName(String name);

    Model createModel(Model model);

    Model updateModel(Long id, Model model);

    void deleteModelByName(String name);
}
