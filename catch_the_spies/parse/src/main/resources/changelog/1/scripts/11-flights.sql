CREATE TABLE wrk.flights (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null,

    dep_date date,
    arr_date date,
    dep_time time,
    arr_time time,

    dep_code varchar references airport(code),
    arr_code varchar references airport(code),
    flight varchar,

    boarding_data_id uuid references boarding_data(id),  -- DONE
    boarding_pass_id uuid references boarding_pass(id),  --
    forum_id uuid references forum_profiles_flights(id), --
    skyteam_id uuid references skyteam_exchange(id),     --
    sirena_id uuid references sirena_export(id),         -- DONE
    pointz_id uuid references pointz_activities(id),     --

    UNIQUE (dep_code, arr_code, user_id, dep_date)
);

-- ALTER TABLE wrk.flights ADD CONSTRAINT flights_uk_1
--     UNIQUE (user_id, dep_date, dep_time, arr_date, arr_time, flight);

CREATE INDEX flights_user_id_idx ON wrk.flights(boarding_data_id);
CREATE INDEX flights_idx1 ON wrk.flights(boarding_pass_id);
CREATE INDEX flights_idx2 ON wrk.flights(forum_id);
CREATE INDEX flights_idx3 ON wrk.flights(skyteam_id);

-- ALTER TABLE wrk.flights ADD COLUMN pointz_id uuid references pointz_activities(id)
