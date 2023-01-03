DROP SCHEMA IF EXISTS car_rest CASCADE;
CREATE SCHEMA car_rest;

CREATE TABLE manufacturer
(
    manufacturer_id bigserial PRIMARY KEY,
    manufacturer    varchar(50) NOT NULL,
    year            int         NOT NULL
);

CREATE TABLE model
(
    model_id bigserial PRIMARY KEY,
    model    varchar(50) NOT NULL
);

CREATE TABLE category
(
    category_id bigserial PRIMARY KEY,
    category    varchar(50)
);

CREATE TABLE car
(
    car_id          bigserial PRIMARY KEY,
    manufacturer_id bigint REFERENCES manufacturer (manufacturer_id) NOT NULL,
    model_id        bigint REFERENCES model (model_id)               NOT NULL
);

CREATE TABLE car_category_info
(
    id          bigserial PRIMARY KEY,
    category_id bigint REFERENCES category (category_id),
    car_id      bigint REFERENCES car (car_id)
);
