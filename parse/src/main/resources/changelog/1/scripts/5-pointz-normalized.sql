CREATE TABLE wrk.pointz_normalized_users (
    id uuid primary key references pointz_users(id),
    first_name varchar,
    last_name varchar,
    uid bigint
);

CREATE INDEX pointz_normalized_last_name_idx ON wrk.pointz_normalized_users(last_name);