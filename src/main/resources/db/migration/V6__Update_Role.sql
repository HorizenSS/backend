-- Step 1: Drop the default value for the `role` column
ALTER TABLE customer ALTER COLUMN role DROP DEFAULT;

-- Step 2: Change the `role` column type to VARCHAR(50)
ALTER TABLE customer ALTER COLUMN role TYPE VARCHAR(50);

-- Step 3: Drop the ENUM type `customer_role` as it's no longer used
DROP TYPE customer_role;
