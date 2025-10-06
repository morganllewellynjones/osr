CREATE SCHEMA IF NOT EXISTS account;

CREATE TABLE IF NOT EXISTS account.roles(
    role TEXT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS account.profile(
    profile_id UUID PRIMARY KEY DEFAULT gen_random_uuid(), 
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role VARCHAR(255) REFERENCES account.roles (role),
    created_timestamp TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_timestamp TIMESTAMPTZ
);


INSERT INTO account.roles (role) VALUES
    ('player'),
    ('storyteller'),
    ('admin');
