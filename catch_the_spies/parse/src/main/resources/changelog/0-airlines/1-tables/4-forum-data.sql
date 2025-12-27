
CREATE TABLE IF NOT EXISTS airlines.forum_profiles (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    nickname VARCHAR NOT NULL,
    sex VARCHAR,
    first_name VARCHAR,
    last_name VARCHAR
);

CREATE TABLE IF NOT EXISTS airlines.airport (
    code VARCHAR PRIMARY KEY,
    city VARCHAR,
    country VARCHAR
);

CREATE TABLE IF NOT EXISTS airlines.forum_profiles_flights (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    profile_id uuid NOT NULL REFERENCES forum_profiles(id),
    "date" DATE NOT NULL,
    depature VARCHAR NOT NULL REFERENCES airport(code),
    arrival VARCHAR NOT NULL REFERENCES airport(code)
);

CREATE TABLE IF NOT EXISTS airlines.loyality_program (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    profile_id uuid NOT NULL REFERENCES forum_profiles(id),
    status VARCHAR NOT NULL,
    program VARCHAR NOT NULL,
    type VARCHAR NOT NULL,
    user_code varchar
);

CREATE INDEX loayality_program_user_code_idx ON loyality_program(user_code);