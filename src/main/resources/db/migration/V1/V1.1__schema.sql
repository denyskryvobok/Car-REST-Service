CREATE TABLE car
(
    car_id       bigserial PRIMARY KEY,
    manufacturer varchar(50) NOT NULL,
    year         int         NOT NULL,
    model        varchar(50) NOT NULL,
    category     varchar(50) NOT NULL
);

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

CREATE TABLE car_category_info
(
    id       bigserial PRIMARY KEY,
    category varchar(50) NOT NULL,
    car_id   bigint REFERENCES car (car_id)
);

CREATE TABLE category
(
    category_id bigserial PRIMARY KEY,
    category    varchar(50)
);
