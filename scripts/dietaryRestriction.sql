CREATE TABLE "dietary_restriction" (
    restriction_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);