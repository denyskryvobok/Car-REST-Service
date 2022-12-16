package com.foxminded.car_rest_service.dao;

import com.foxminded.car_rest_service.entities.Car;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CarDAO extends JpaRepository<Car, Long> {

    @Query(value = "SELECT c " +
                   "FROM Car c " +
                   "JOIN FETCH c.model m " +
                   "JOIN FETCH c.manufacturer mf " +
                   "JOIN FETCH c.carCategories cc " +
                   "JOIN FETCH cc.category ct ")
    List<Car> findAllCarsWithAllInfo(Pageable pageable);

    @Query(value = "SELECT c " +
                   "FROM Car c " +
                   "WHERE c.manufacturer.manufacturer = :manufacturer " +
                            "AND c.manufacturer.year = :year " +
                            "AND c.model.model = :model")
    Optional<Car> findByManufacturerAndModelAndYear(String manufacturer, String model, Integer year);

    @Query(value = "SELECT c FROM Car c JOIN FETCH c.carCategories cc WHERE c.id = :id ")
    Optional<Car> findCarWithCategoriesById(Long id);

    @Query(value = "SELECT c FROM Car c " +
            "JOIN FETCH c.model m " +
            "JOIN FETCH c.manufacturer mf "  +
            "JOIN FETCH c.carCategories cc " +
            "JOIN FETCH cc.category ct " +
            "JOIN FETCH cc.category WHERE c.id = :id ")
    Optional<Car> findCarWithAllInfoById(Long id);

    @Query(value = "SELECT c FROM Car c " +
                   "JOIN FETCH c.model m " +
                   "JOIN FETCH c.manufacturer mf "  +
                   "JOIN FETCH c.carCategories cc " +
                   "JOIN FETCH cc.category ct " +
                   "JOIN FETCH cc.category " +
                   "WHERE c.manufacturer.manufacturer = :manufacturer ")
    List<Car> findCarsByManufacturer(String manufacturer, Pageable pageable);

    @Query(value = "SELECT c FROM Car c " +
                   "JOIN FETCH c.model m " +
                   "JOIN FETCH c.manufacturer mf "  +
                   "JOIN FETCH c.carCategories cc " +
                   "JOIN FETCH cc.category ct " +
                   "JOIN FETCH cc.category " +
                   "WHERE c.manufacturer.manufacturer = :manufacturer AND c.manufacturer.year >= :year")
    List<Car> findCarsByManufacturerAndMinYear(String manufacturer, Integer year, Pageable pageable);
}
