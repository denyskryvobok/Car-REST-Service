package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.dao.ModelDAO;
import com.foxminded.car_rest_service.entities.Model;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelDTO;
import com.foxminded.car_rest_service.mapstruct.mapper.ModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
            return null;
        });

        return mapper.modelToModelDTO(model);
    }

    @Override
    public ModelBasicDTO createModel(ModelBasicDTO modelBasicDTO) {
        log.info("CreateModel started with input: {}", modelBasicDTO);
        Optional<Model> optional = modelDAO.findByName(modelBasicDTO.getModel());

        if (optional.isPresent()) {
            return null;

        }
        return mapper.modelToModelBasicDTO(modelDAO.save(mapper.modelBasicDTOToModel(modelBasicDTO)));
    }

    @Override
    public ModelBasicDTO updateModel(Long id, ModelBasicDTO modelBasicDTO) {
        log.info("UpdateModel started with id: {}, model: {}", id, modelBasicDTO);

        return modelDAO.findById(id).map(m -> {
            m.setModel(modelBasicDTO.getModel());
            return mapper.modelToModelBasicDTO(modelDAO.save(m));
        }).orElse(null);

    }

    @Override
    public boolean deleteModelByName(String name) {
        log.info("DeleteModelByName started with name: {}", name);

        return modelDAO.findModelWithCarsByName(name)
                .map(m -> {
                    modelDAO.delete(m);
                    return true;
                }).orElse(false);
    }
}
