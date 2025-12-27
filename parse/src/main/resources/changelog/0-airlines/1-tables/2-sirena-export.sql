-- DROP TABLE IF EXISTS airlines.sirena_export;

CREATE TABLE IF NOT EXISTS airlines.sirena_export (
    id uuid default gen_random_uuid() primary key,
    pax_name VARCHAR,
    pax_birth_data DATE,
    departure_date DATE,
    departure_time TIME WITHOUT TIME ZONE,
    arrival_date DATE,
    arrival_time TIME WITHOUT TIME ZONE,
    flight VARCHAR,
    code_sh BOOLEAN,
    "from" VARCHAR,
    dest VARCHAR,
    code VARCHAR,
    eticket VARCHAR,
    travel_doc VARCHAR,
    seat VARCHAR,
    meal VARCHAR,
    trv_cls VARCHAR,
    fare VARCHAR,
    baggage VARCHAR,
    pax_additional_info VARCHAR,
    agent_info VARCHAR
)