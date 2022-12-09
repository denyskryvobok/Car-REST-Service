package com.foxminded.car_rest_service.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "car_categories")
public class CarCategory {
    @EmbeddedId
    private CarCategoryPK carCategoryPK;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("carId")
    @JoinColumn(name = "car_id")
    @ToString.Exclude
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private Category category;

    public CarCategory(Car car, Category category) {
        this.car = car;
        this.category = category;
        this.carCategoryPK = new CarCategoryPK(car.getId(), category.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CarCategory that = (CarCategory) o;
        return carCategoryPK != null && Objects.equals(carCategoryPK, that.carCategoryPK);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carCategoryPK);
    }
}
