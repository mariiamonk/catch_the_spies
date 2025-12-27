

WITH
joined AS (
    SELECT bp.id as id, u.id as user_id FROM wrk.boarding_pass_name_norm bp
    JOIN wrk.users u ON u.first_name = bp.part_1 AND u.last_name = bp.part_2
    UNION ALL
    SELECT bp.id as id, u.id as user_id FROM wrk.boarding_pass_name_norm bp
    JOIN wrk.users u ON u.first_name = bp.part_2 AND u.last_name = bp.part_1
),
filtred AS (
    SELECT id, any_value(user_id) as user_id FROM joined
        GROUP BY id
        HAVING count(distinct user_id) = 1
)
UPDATE wrk.boarding_pass_name_norm bp
SET user_id = t.user_id
FROM filtred t
WHERE t.id = bp.id;





