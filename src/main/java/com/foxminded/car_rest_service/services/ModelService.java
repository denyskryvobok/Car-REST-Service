package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.mapstruct.dto.model.ModelBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ModelService {
    List<ModelBasicDTO> getAllModels(Pageable pageable);

    ModelDTO getModelWithCarsByName(String name);

    ModelBasicDTO createModel(ModelBasicDTO modelBasicDTO);

    ModelBasicDTO updateModel(Long id, ModelBasicDTO modelBasicDTO);

    boolean deleteModelByName(String name);
}
