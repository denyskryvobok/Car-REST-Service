package com.foxminded.car_rest_service.mapstruct.mapper;

import com.foxminded.car_rest_service.entities.Model;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModelMapper extends MainMapper{

    ModelBasicDTO modelToModelBasicDTO(Model model);

    Model modelBasicDTOToModel(ModelBasicDTO modelBasicDTO);

    ModelDTO modelToModelDTO(Model model);

}
