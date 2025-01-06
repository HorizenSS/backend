CREATE TYPE customer_role AS ENUM ('ADMIN', 'USER');

-- Add role column with default value
ALTER TABLE customer ADD role customer_role DEFAULT 'USER';
