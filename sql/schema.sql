/*
DROP DATABASE "bankmanager";
CREATE DATABASE "bankmanager";

DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS banks;
DROP TABLE IF EXISTS users;
*/

create TABLE IF NOT EXISTS  banks (
    id BIGSERIAL PRIMARY KEY,
    name varchar (50) NOT NULL,
    deleted boolean NOT NULL DEFAULT FALSE
);

create TABLE IF NOT EXISTS  users (
    id BIGSERIAL PRIMARY KEY,
    first_name varchar (15) NOT NULL,
    last_name varchar (15) NOT NULL,
    deleted boolean NOT NULL DEFAULT FALSE
);

create TABLE IF NOT EXISTS  accounts (
    id BIGSERIAL PRIMARY KEY,
    number BIGINT NOT NULL,
    amount NUMERIC(25,2) NOT NULL DEFAULT 0,
    cashback_last_date DATE,
    bank_id BIGINT NOT NULL REFERENCES banks,
    user_id BIGINT NOT NULL REFERENCES users,
    deleted boolean NOT NULL DEFAULT FALSE

);

create TABLE IF NOT EXISTS  transactions (
    id BIGSERIAL PRIMARY KEY,
    amount NUMERIC(25,2) NOT NULL,
    date_time TIMESTAMP NOT NULL,
    recipient_id BIGINT NOT NULL REFERENCES accounts,
    sender_id BIGINT REFERENCES accounts,
    deleted boolean NOT NULL DEFAULT FALSE
);