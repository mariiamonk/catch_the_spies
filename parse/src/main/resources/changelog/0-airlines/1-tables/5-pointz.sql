
CREATE TABLE IF NOT EXISTS airlines.pointz_users (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    uid BIGINT NOT NULL,
    first_name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS airlines.pointz_activities (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id uuid NOT NULL REFERENCES airlines.pointz_users(id),
    bonus_program VARCHAR NOT NULL,
    number VARCHAR NOT NULL,
    code VARCHAR NOT NULL,
    "date" DATE NOT NULL,
    departure VARCHAR NOT NULL REFERENCES airlines.airport (code),
    arrival VARCHAR NOT NULL REFERENCES airlines.airport (code),
    fare VARCHAR NOT NULL
);
