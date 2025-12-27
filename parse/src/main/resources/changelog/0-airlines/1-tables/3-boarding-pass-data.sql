
CREATE TABLE IF NOT EXISTS airlines.boarding_pass(
    id uuid default gen_random_uuid() primary key,
    name VARCHAR,
    flight VARCHAR,
    "from" VARCHAR,
    "to" VARCHAR,
    from_code VARCHAR,
    to_code VARCHAR,
    date DATE,
    "time" TIME WITHOUT TIME ZONE,
    operated_by VARCHAR,
    seat VARCHAR,
    pnr VARCHAR,
    etiket VARCHAR,
    sequence INT
)