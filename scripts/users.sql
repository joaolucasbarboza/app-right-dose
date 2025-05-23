DROP TABLE users;
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
