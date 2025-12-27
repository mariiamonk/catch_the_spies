
CREATE TABLE IF NOT EXISTS airlines.sirena_export_users(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    pax_name VARCHAR,
    first_name VARCHAR,
    last_name VARCHAR,
    second_name VARCHAR,
    document VARCHAR,
    birth_date DATE
);

ALTER TABLE airlines.sirena_export ADD COLUMN IF NOT EXISTS user_id uuid REFERENCES sirena_export_users(id);