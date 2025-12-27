

CREATE VIEW wrk.flights_aggregate_v AS
SELECT agg.* FROM wrk.flights_aggregate agg
LEFT JOIN