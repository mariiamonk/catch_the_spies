
WITH
boarding_data_ext AS (
    SELECT *, u.id as user_id_agg, bd.id as bd_id FROM boarding_data bd
    LEFT JOIN wrk.flights_dep_arr f
         ON bd.flight_number = f.flight
             AND (bd.destination = f.arr_city OR f.arr_city IS NULL)
    LEFT JOIN wrk.users u on u.boarding_data_user_id = bd.user_id
)
INSERT INTO wrk.flights (user_id, dep_date, dep_time, flight, dep_code, arr_code, boarding_data_id)
SELECT user_id_agg, flight_date, flight_time, flight_number, dep, arr, bd_id
FROM boarding_data_ext;



INSERT INTO wrk.flights (user_id, dep_date, dep_time, arr_date, arr_time, dep_code, arr_code, flight, sirena_id)
SELECT u.id, se.departure_date, se.departure_time, se.arrival_date, se.arrival_time,
       se."from", se.dest, se.flight, se.id
FROM sirena_export se
    LEFT JOIN wrk.users u ON u.sirena_user_id = se.user_id
ON CONFLICT (dep_code, arr_code, user_id, dep_date) DO UPDATE SET
    arr_date = excluded.arr_date,
    arr_time = excluded.arr_time,
    dep_time = excluded.dep_time,
    flight = excluded.flight,
    sirena_id = excluded.sirena_id;


WITH
pointz_skyteam AS (
    SELECT *
    FROM pointz_activities p
    FULL JOIN skyteam_exchange s ON
        p.number = s.user_code
        AND p.code = s.flight_code
        AND p.date = s.date
)

SELECT * FROM pointz_skyteam ;


SELECT * FROM pointz_activities pa JOIN pointz_users pu ON pa.user_id = pu.id;



SELECT * FROM loyality_program lp
    JOIN skyteam_exchange se ON lp.program || lp.type = se.user_code

-- SELECT * FROM wrk.flights WHERE boarding_data_id IS NOT NULL AND sirena_id IS NOT NULL