
CREATE TABLE wrk.boarding_pass_name_norm (
    id uuid primary key references boarding_pass(id),
    part_1 varchar,
    part_2 varchar,
    part_3 varchar,
    first_name varchar,
    last_name varchar,
    second_name varchar
)