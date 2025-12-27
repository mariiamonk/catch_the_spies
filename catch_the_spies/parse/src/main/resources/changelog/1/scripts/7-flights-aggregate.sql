
CREATE TABLE wrk.flights_aggregate (
    id uuid primary key default gen_random_uuid(),
    flight_code varchar,
    flight_date date,
    departure varchar references airport(code),
    arrival varchar references airport(code),

    user_code varchar,
    user_fare varchar,
    pnr varchar,
    eticket varchar,

    boarding_pass_id uuid references boarding_pass(id),
    sirena_id uuid references sirena_export(id),
    skyteam_id uuid references skyteam_exchange(id),
    boarding_data uuid references boarding_data(id)
);
-- KEYS
-- flight_code, pnr
-- flight_code, user_code, flight_date
-- eticket

CREATE UNIQUE INDEX flights_aggregate_uk_1 ON wrk.flights_aggregate(eticket);
CREATE UNIQUE INDEX flights_aggregate_uk_2 ON wrk.flights_aggregate(flight_code, user_code, flight_date);
CREATE UNIQUE INDEX flights_aggregate_uk_3 ON wrk.flights_aggregate(flight_code, pnr);


--