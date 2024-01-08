SHOW DATABASES;

CREATE DATABASE spring_restful_api;

USE spring_restful_api;

CREATE TABLE users
(
    username     VARCHAR(255) NOT NULL,
    password     VARCHAR(255) NOT NULL,
    name         VARCHAR(255) NOT NULL,
    access_token VARCHAR(255),
    expired_at   INT,
    PRIMARY KEY (username),
    UNIQUE (access_token)
) ENGINE InnoDB;

CREATE TABLE contacts
(
    id       VARCHAR(255) NOT NULL,
    name     VARCHAR(255) NOT NULL,
    phone    VARCHAR(255),
    email    VARCHAR(255),
    username VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY fk_users_contacts (username) REFERENCES users (username)
) ENGINE InnoDB;

CREATE TABLE addresses
(
    id         VARCHAR(255) NOT NULL,
    street     VARCHAR(255),
    city       VARCHAR(255),
    country    VARCHAR(255) NOT NULL,
    contact_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY fk_contacts_addresses (contact_id) REFERENCES contacts (id)
) ENGINE InnoDB;

SELECT *
FROM users;

SELECT *
FROM contacts;