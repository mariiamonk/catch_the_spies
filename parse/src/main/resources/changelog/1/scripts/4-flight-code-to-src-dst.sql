-- DROP TABLE wrk.flights_dep_arr;
CREATE TABLE wrk.flights_dep_arr (
    flight varchar not null,
    dep varchar not null,
    arr varchar not null,

    dep_city varchar,
    arr_city varchar
);


ALTER TABLE wrk.flights_dep_arr ADD CONSTRAINT flights_dep_arr_uk UNIQUE (flight, dep, arr);
CREATE INDEX flight_dep_arr_flight_code_idx ON wrk.flights_dep_arr(flight);
--
INSERT INTO wrk.flights_dep_arr (flight, dep, arr)
SELECT flight, "from", dest FROM sirena_export
    GROUP BY flight, "from", dest;

UPDATE wrk.flights_dep_arr fl
SET arr_city = t.city
FROM airport t
WHERE fl.arr = t.code;


