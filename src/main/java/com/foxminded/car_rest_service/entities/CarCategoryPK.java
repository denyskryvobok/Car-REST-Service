package com.foxminded.car_rest_service.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class CarCategoryPK implements Serializable {

    @Column(name = "car_id")
    private Long carId;

    @Column(name = "category_id")
    private Long categoryId;
}
