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

INSERT INTO cars (manufacturer, year, model, category)
SELECT manufacturer, year, model, category
FROM t;

DROP TABLE t;


-- FILLING THE 'manufacturers' TABLE

INSERT INTO manufacturers (manufacturer, year)
SELECT DISTINCT manufacturer, year
FROM cars;

ALTER TABLE cars
    ADD manufacturer_id bigint REFERENCES manufacturers (manufacturer_id);

UPDATE cars
SET manufacturer_id = m.manufacturer_id
FROM manufacturers m
WHERE m.manufacturer = cars.manufacturer
  AND m.year = cars.year;

ALTER TABLE cars
    DROP COLUMN manufacturer;

ALTER TABLE cars
    DROP COLUMN year;


-- FILLING THE 'models' TABLE

INSERT INTO models (model)
SELECT DISTINCT model
FROM cars;

ALTER TABLE cars
    ADD model_id bigint REFERENCES models (model_id);

UPDATE cars
SET model_id = m.model_id
FROM models m
WHERE m.model = cars.model;

ALTER TABLE cars
    DROP COLUMN model;


-- FILLING THE 'car_categories' AND 'categories' TABLE

CREATE FUNCTION insert_car_categorise() RETURNS void AS
$$
DECLARE
    car             RECORD;
    arr_category    varchar(50);
    nested_category varchar(50);
BEGIN
    FOR car IN SELECT car_id, category FROM cars
        LOOP
            FOREACH arr_category IN ARRAY string_to_array(car.category, ', ')
                LOOP
                    FOREACH nested_category IN ARRAY string_to_array(arr_category, ',')
                        LOOP
                            INSERT INTO car_categories VALUES (nested_category, car.car_id);
                        end loop;
                end loop;
        end loop;
END;
$$ LANGUAGE plpgsql;

SELECT insert_car_categorise();

ALTER TABLE cars
    DROP COLUMN category;

INSERT INTO categories (category)
SELECT DISTINCT category
FROM car_categories;

ALTER TABLE car_categories
    ADD category_id bigint REFERENCES categories (category_id);

UPDATE car_categories
SET category_id = ct.category_id
FROM categories ct
WHERE ct.category = car_categories.category;

ALTER TABLE car_categories
    DROP COLUMN category;
