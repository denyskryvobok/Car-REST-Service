CREATE TABLE cars
(
    car_id       bigserial PRIMARY KEY,
    manufacturer varchar(50) NOT NULL,
    year         int         NOT NULL,
    model        varchar(50) NOT NULL,
    category     varchar(50) NOT NULL
);

CREATE TABLE manufacturers
(
    manufacturer_id bigserial PRIMARY KEY,
    manufacturer    varchar(50) NOT NULL,
    year            int         NOT NULL
);

CREATE TABLE models
(
    model_id bigserial PRIMARY KEY,
    model    varchar(50) NOT NULL
);

CREATE TABLE car_categories
(
    category varchar(50) NOT NULL,
    car_id   bigint REFERENCES cars (car_id)
);

CREATE TABLE categories
(
    category_id bigserial PRIMARY KEY,
    category    varchar(50)
);
