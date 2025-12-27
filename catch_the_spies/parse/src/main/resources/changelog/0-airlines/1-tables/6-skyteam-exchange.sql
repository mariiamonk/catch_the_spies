
CREATE TABLE IF NOT EXISTS airlines.skyteam_exchange (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_code VARCHAR NOT NULL,
    user_fare VARCHAR NOT NULL,
    user_cls VARCHAR NOT NULL,
    flight_code VARCHAR,
    "date" DATE NOT NULL,
    departure VARCHAR NOT NULL REFERENCES airlines.airport(code),
    arrival VARCHAR NOT NULL REFERENCES airlines.airport(code),
    status VARCHAR
);
