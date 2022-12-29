package com.foxminded.car_rest_service.dao;

import com.foxminded.car_rest_service.entities.Model;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ModelDAO extends JpaRepository<Model, Long> {

    @Query(value = "SELECT m FROM Model m ")
    List<Model> findAllPage(Pageable pageable);

    @Query(value = "SELECT m " +
                   "FROM Model m " +
                   "JOIN FETCH m.cars c " +
                   "JOIN FETCH c.manufacturer mf " +
                   "JOIN FETCH c.carCategories cc " +
                   "JOIN FETCH cc.category ct " +
                   "WHERE m.model = :name ")
    Optional<Model> findModelWithCarsByName(String name);

    @Query(value = "SELECT m FROM Model m WHERE m.model = :model")
    Optional<Model> findByName(String model);
}
