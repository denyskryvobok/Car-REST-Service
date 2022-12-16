package com.foxminded.car_rest_service.mapstruct.mapper;

import com.foxminded.car_rest_service.entities.Car;
import com.foxminded.car_rest_service.entities.CarCategoryInfo;
import com.foxminded.car_rest_service.entities.Category;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarWithoutCategoriesDTO;
import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends MainMapper {

    CarWithoutCategoriesDTO carToCarWithoutCategoriesDTO(Car car);

    default Set<CarWithoutCategoriesDTO> carCategoriesToCarWithoutCategoriesDTO(Set<CarCategoryInfo> categoryInfo) {
        if (categoryInfo == null) {
            return null;
        }

        Set<CarWithoutCategoriesDTO> carDTO = new HashSet<>();

        for (CarCategoryInfo carCategory : categoryInfo) {
            carDTO.add(carToCarWithoutCategoriesDTO(carCategory.getCar()));
        }

        return carDTO;
    }

    CategoryBasicDTO categoryToCategoryBasicDTO(Category category);

    Category categoryBasicToCategory(CategoryBasicDTO categoryBasicDTO);

    @Mapping(source = "category.carCategories", target = "cars")
    CategoryDTO categoryToCategoryDTO(Category category);
}
