package com.foxminded.car_rest_service.mapstruct.mapper;

import com.foxminded.car_rest_service.entities.Manufacturer;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ManufacturerMapper extends MainMapper {

    ManufacturerDTO manufacturerToManufacturerDTO(Manufacturer manufacturer);


    ManufacturerBasicDTO manufacturerToManufacturerBasicDTO(Manufacturer manufacturer);

    Manufacturer manufacturerBasicDTOToManufacturer(ManufacturerBasicDTO manufacturerBasicDTO);

}
