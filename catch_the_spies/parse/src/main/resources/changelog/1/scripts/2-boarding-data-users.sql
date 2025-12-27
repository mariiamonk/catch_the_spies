
CREATE TABLE IF NOT EXISTS airlines.boarding_data_users (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR,
    last_name VARCHAR,
    second_name VARCHAR,
    sex VARCHAR,
    document VARCHAR,
    birth_date VARCHAR
);

ALTER TABLE airlines.boarding_data ADD COLUMN IF NOT EXISTS user_id uuid REFERENCES airlines.boarding_data_users(id);
