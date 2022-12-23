CREATE TABLE app_user
(
    user_id  bigserial PRIMARY KEY,
    username varchar(50) UNIQUE NOT NULL,
    password text               NOT NULL,
    disabled bool
);

CREATE TABLE role
(
    role_id serial PRIMARY KEY,
    name    varchar(30) UNIQUE NOT NULL
);

CREATE TABLE app_user_role
(
    id      bigserial PRIMARY KEY,
    user_id bigint REFERENCES app_user (user_id),
    role_id bigint REFERENCES role (role_id)
);

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
