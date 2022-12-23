package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.mapstruct.mapper.CarMapper;
import com.foxminded.car_rest_service.mapstruct.mapper.CarMapperImpl;
import com.foxminded.car_rest_service.mapstruct.mapper.CategoryMapper;
import com.foxminded.car_rest_service.mapstruct.mapper.CategoryMapperImpl;
import com.foxminded.car_rest_service.mapstruct.mapper.ManufacturerMapper;
import com.foxminded.car_rest_service.mapstruct.mapper.ManufacturerMapperImpl;
import com.foxminded.car_rest_service.mapstruct.mapper.ModelMapper;
import com.foxminded.car_rest_service.mapstruct.mapper.ModelMapperImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class TestConfig {

    @Bean
    CarService carService() {
        return new CarServiceImp();
    }

    @Bean
    ManufacturerService manufacturerService() {
        return new ManufacturerServiceImp();
    }

    @Bean
    ModelService modelService() {
        return new ModelServiceImp();
    }

    @Bean
    CategoryService categoryService() {
        return new CategoryServiceImp();
    }

    @Bean
    CarMapper carMapper() {
        return new CarMapperImpl();
    }

    @Bean
    CategoryMapper categoryMapper() {
        return new CategoryMapperImpl();
    }

    @Bean
    ManufacturerMapper manufacturerMapper() {
        return new ManufacturerMapperImpl();
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapperImpl();
    }

}
