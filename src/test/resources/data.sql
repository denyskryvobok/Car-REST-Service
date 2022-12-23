INSERT INTO model(model)
VALUES ('Touareg 2'),
       ('Regal'),
       ('Grand');

INSERT INTO role(name)
VALUES ('ADMIN'),
       ('USER');

INSERT INTO app_user (username, password, disabled)
VALUES ('jamessmith', '$2a$10$k82frkwHnma39AQH9WEOUuY1l102RFjI/CP8xHZdQZPv9BF0QAPfu', false),
       ('maryjones', '$2a$10$k82frkwHnma39AQH9WEOUuY1l102RFjI/CP8xHZdQZPv9BF0QAPfu', false),
       ('roberttaylor', '$2a$10$k82frkwHnma39AQH9WEOUuY1l102RFjI/CP8xHZdQZPv9BF0QAPfu', false);

INSERT INTO app_user_role(user_id, role_id)
VALUES (1, 1),
       (2, 2),
       (3, 2);

INSERT INTO manufacturer(manufacturer, year)
VALUES ('Acura', 2017),
       ('Acura', 2005),
       ('Acura', 2006),
       ('Aston Martin', 2010),
       ('Aston Martin', 2012),
       ('Aston Martin', 2018);

INSERT INTO category(category)
VALUES ('SUV1992'),
       ('Convertible'),
       ('Wagon');

INSERT INTO car(manufacturer_id, model_id)
VALUES (1, 1),
       (2, 1),
       (3, 2),
       (4, 2),
       (4, 3),
       (5, 3),
       (6, 1);

INSERT INTO car_category_info(car_id, category_id)
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (3, 3),
       (4, 1),
       (4, 3),
       (5, 2),
       (6, 3),
       (7, 1);
