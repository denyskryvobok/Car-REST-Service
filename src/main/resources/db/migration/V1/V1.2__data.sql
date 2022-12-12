-- INIT DATA

CREATE TEMPORARY TABLE t
(
    id           text,
    manufacturer text,
    year         int,
    model        text,
    category     text
);


COPY t
    FROM '/tmp/db_init.csv'
    WITH (FORMAT CSV, HEADER);

INSERT INTO car (manufacturer, year, model, category)
SELECT manufacturer, year, model, category
FROM t;

DROP TABLE t;


-- FILLING THE 'manufacturer' TABLE

INSERT INTO manufacturer (manufacturer, year)
SELECT DISTINCT manufacturer, year
FROM car;

ALTER TABLE car
    ADD manufacturer_id bigint REFERENCES manufacturer (manufacturer_id);

UPDATE car
SET manufacturer_id = m.manufacturer_id
FROM manufacturer m
WHERE m.manufacturer = car.manufacturer
  AND m.year = car.year;

ALTER TABLE car
    DROP COLUMN manufacturer;

ALTER TABLE car
    DROP COLUMN year;


-- FILLING THE 'model' TABLE

INSERT INTO model (model)
SELECT DISTINCT model
FROM car;

ALTER TABLE car
    ADD model_id bigint REFERENCES model (model_id);

UPDATE car
SET model_id = m.model_id
FROM model m
WHERE m.model = car.model;

ALTER TABLE car
    DROP COLUMN model;


-- FILLING THE 'car_category_info' AND 'category' TABLE

CREATE FUNCTION insert_car_category_info() RETURNS void AS
$$
DECLARE
    car             RECORD;
    arr_category    varchar(50);
    nested_category varchar(50);
BEGIN
    FOR car IN SELECT car_id, category FROM car
        LOOP
            FOREACH arr_category IN ARRAY string_to_array(car.category, ', ')
                LOOP
                    FOREACH nested_category IN ARRAY string_to_array(arr_category, ',')
                        LOOP
                            INSERT INTO car_category_info(category, car_id) VALUES (nested_category, car.car_id);
                        end loop;
                end loop;
        end loop;
END;
$$ LANGUAGE plpgsql;

SELECT insert_car_category_info();

ALTER TABLE car
    DROP COLUMN category;

INSERT INTO category (category)
SELECT DISTINCT category
FROM car_category_info;

ALTER TABLE car_category_info
    ADD category_id bigint REFERENCES category (category_id);

UPDATE car_category_info
SET category_id = ct.category_id
FROM category ct
WHERE ct.category = car_category_info.category;

ALTER TABLE car_category_info
    DROP COLUMN category;
