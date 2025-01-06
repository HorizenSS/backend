-- Create enum types
--CREATE TYPE  gender_type AS ENUM ('MALE', 'FEMALE');
--CREATE TYPE alert_type AS ENUM ('SUSPICIOUS_ACTIVITY', 'CRIME', 'HAZARD', 'EMERGENCY', 'OTHER', 'WEATHER', 'TRAFFIC', 'ENVIRONMENTAL', 'PUBLIC_SAFETY', 'HEALTH', 'TRANSPORTATION', 'FIRE', 'NATURAL_DISASTER');
--CREATE TYPE alert_status AS ENUM ('ACTIVE', 'RESOLVED', 'FALSE_ALARM');
--CREATE TYPE alert_severity AS ENUM ('LOW', 'MEDIUM', 'HIGH');
-- Create customer table
CREATE TABLE if not exists customer (
                          id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                          name VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL,
                          age INTEGER NOT NULL,
                          gender gender_type NOT NULL,
                          password VARCHAR(255) NOT NULL,
                          profile_image_id VARCHAR(255),
                          CONSTRAINT customer_email_unique UNIQUE (email),
                          CONSTRAINT profile_image_id_unique UNIQUE (profile_image_id)
);

-- Create alerts table
CREATE TABLE IF NOT EXISTS alerts (
                        id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                        title VARCHAR(255) NOT NULL,
                        description TEXT NOT NULL,
                        type alert_type NOT NULL,
                        latitude DOUBLE PRECISION NOT NULL,
                        longitude DOUBLE PRECISION NOT NULL,
                        user_id INTEGER NOT NULL,
                        created_at TIMESTAMP NOT NULL,
                        updated_at TIMESTAMP,
                        status alert_status NOT NULL DEFAULT 'ACTIVE',
                        severity alert_severity NOT NULL DEFAULT 'MEDIUM',
                        CONSTRAINT fk_customer
                            FOREIGN KEY (user_id)
                                REFERENCES customer(id)
                                ON DELETE CASCADE
);
-- Execute this script in the database
CREATE SEQUENCE if not exists customer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
