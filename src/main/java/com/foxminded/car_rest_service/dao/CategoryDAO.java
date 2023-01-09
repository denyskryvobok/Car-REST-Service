package com.foxminded.car_rest_service.dao;

import com.foxminded.car_rest_service.entities.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryDAO extends JpaRepository<Category, Long> {

    @Query(value = "SELECT c FROM Category c ")
    List<Category> findAllPage(Pageable pageable);

    @Query(value = "SELECT c FROM Category c " +
                   "LEFT JOIN FETCH c.carCategories cc " +
                   "LEFT JOIN FETCH cc.car cr " +
                   "LEFT JOIN FETCH cr.model m " +
                   "LEFT JOIN FETCH cr.manufacturer mf " +
                   "WHERE c.category = :name ")
    Optional<Category> findCategoryWithCarsByName(String name);

    @Query(value = "SELECT c FROM Category c WHERE c.category = :category")
    Optional<Category> findByName(String category);
}
