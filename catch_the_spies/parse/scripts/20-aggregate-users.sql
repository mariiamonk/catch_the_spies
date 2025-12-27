
WITH
pointz_users_to_users AS (
    SELECT u.id as user_id, pu.id as pointz_id, pu.uid as uid, pu.first_name as first_name, pu.last_name as last_name
        FROM wrk.pointz_normalized_users pu
        JOIN wrk.users u ON u.last_name = pu.last_name and u.first_name = pu.first_name
),
uniquely_mapped_pointz_users_names AS (
    SELECT first_name, last_name FROM pointz_users_to_users
        GROUP BY first_name, last_name
        HAVING count(distinct user_id) = 1 AND count(distinct pointz_id) = 1
),
uniquely_mapped_pointz_users AS (
    SELECT n.first_name, n.last_name, u.id as user_id, pnu.id as pointz_id FROM uniquely_mapped_pointz_users_names n
         JOIN wrk.users u ON n.first_name = u.first_name and n.last_name = u.last_name
         JOIN wrk.pointz_normalized_users pnu ON n.first_name = pnu.first_name AND n.last_name = pnu.last_name
)
UPDATE wrk.users u
SET pointz_id = p.pointz_id
FROM uniquely_mapped_pointz_users p
WHERE u.id = p.user_id;


UPDATE loyality_program SET user_code = program || type;