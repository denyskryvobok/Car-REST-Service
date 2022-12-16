package com.foxminded.car_rest_service.dao;

import com.foxminded.car_rest_service.entities.Manufacturer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ManufacturerDAO extends JpaRepository<Manufacturer, Long> {

    @Query(value = "SELECT DISTINCT m.manufacturer FROM Manufacturer m ")
    List<String> findAllUniqueManufacturers(Pageable pageable);

    @Query(value = "SELECT m FROM Manufacturer m WHERE m.manufacturer = :name AND m.year = :year")
    Optional<Manufacturer> findByNameAndYear(String name, Integer year);

    @Query(value = "SELECT m " +
                   "FROM Manufacturer m " +
                   "JOIN FETCH m.cars c " +
                   "JOIN FETCH c.carCategories cc " +
                   "WHERE m.manufacturer = :name ")
    Set<Manufacturer> findAllByName(String name);

    @Query(value = "SELECT m " +
                   "FROM Manufacturer m " +
                   "JOIN FETCH m.cars c " +
                   "JOIN FETCH c.carCategories cc " +
                   "WHERE m.manufacturer = :name AND m.year = :year")
    Optional<Manufacturer> findByNameAndYearWithCars(String name, Integer year);

    @Query(value = "SELECT mf " +
                   "FROM Manufacturer mf " +
                   "LEFT JOIN FETCH mf.cars c " +
                   "LEFT JOIN FETCH c.model m " +
                   "LEFT JOIN FETCH c.carCategories cc " +
                   "LEFT JOIN FETCH cc.category ct " +
                   "WHERE mf.manufacturer = :name ")
    List<Manufacturer> findAllManufacturersWithCarsByName(String name, Pageable pageable);
}
