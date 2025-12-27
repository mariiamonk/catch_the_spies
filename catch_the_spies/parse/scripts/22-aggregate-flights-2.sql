
-- sierna_export
INSERT INTO wrk.flights_aggregate
(flight_date, flight_code, departure, arrival, pnr, eticket, sirena_id)
SELECT departure_date, flight, "from", dest, code, eticket, id
FROM sirena_export;

-- boarding_pass
INSERT INTO wrk.flights_aggregate
(flight_date, flight_code, departure, arrival, pnr, eticket, boarding_pass_id)
SELECT "date", flight, from_code, to_code, pnr, etiket, id
    FROM boarding_pass
ON CONFLICT (eticket) DO
    UPDATE SET
        boarding_pass_id = excluded.boarding_pass_id;

-- verify no conflicts
SELECT * FROM wrk.flights_aggregate f
JOIN boarding_pass bp ON bp.id = f.boarding_pass_id
WHERE  f.flight_date != bp."date"
    OR f.departure != bp.from_code
    OR f.arrival != bp.to_code
    OR f.pnr != bp.pnr; -- empty - OK





INSERT INTO wrk.flights_aggregate (user_code, departure, arrival, flight_date, flight_code)
SELECT user_code, departure, arrival, date, flight_code FROM skyteam_exchange;


