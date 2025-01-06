ALTER TABLE customer
ADD COLUMN if not exists profile_image_id VARCHAR(36);

--ALTER TABLE customer
--ADD CONSTRAINT profile_image_id_unique UNIQUE (profile_image_id);