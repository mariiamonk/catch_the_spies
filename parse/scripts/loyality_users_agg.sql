
INSERT INTO wrk.user_code_agg (user_code)
SELECT number FROM pointz_activities
UNION ALL
SELECT user_code FROM forum_profiles WHERE forum_profiles.user_code IS NOT NULL
ON CONFLICT DO NOTHING;

UPDATE wrk.user_code_agg
SET pointz_user_id = t.user_id
FROM (
    SELECT user_id, number FROM pointz_activities
) t WHERE user_code = t.number;

UPDATE wrk.user_code_agg agg
SET forum_profile_id = t.id
FROM (
    SELECT id, user_code FROM forum_profiles
) t WHERE agg.user_code = t.user_code;



WITH
    aggregate_name AS (
        SELECT
            agg.user_code,
            CASE
                WHEN pu.first_name IS NULL THEN fp.first_name
                WHEN fp.first_name IS NULL THEN pu.first_name
                WHEN pu.first_name != fp.first_name THEN 'UNDEF'
                END as first_name,
            CASE
                WHEN pu.last_name IS NULL THEN fp.last_name
                WHEN fp.last_name IS NULL THEN pu.last_name
                WHEN pu.last_name != fp.last_name THEN 'UNDEF'
                END as last_name
        FROM wrk.user_code_agg agg
                 LEFT JOIN wrk.pointz_normalized_users pu ON pu.id = agg.pointz_user_id
                 LEFT JOIN wrk.forum_profiles_normalized fp ON fp.id = agg.forum_profile_id
    )
UPDATE wrk.user_code_agg agg
SET first_name = t.first_name,
    last_name = t.last_name
FROM aggregate_name t
WHERE agg.user_code = t.user_code;
-- Конфликтов не найденно


-- Сохранить в users пользователей с ФИ которого нет в users
-- WITH
-- users_names_not_presented_in_users AS (
--     SELECT ag.first_name, ag.last_name FROM wrk.user_code_agg ag
--     LEFT JOIN wrk.users u ON
--         u.first_name = ag.first_name AND u.last_name = ag.last_name
--     WHERE u.id IS NULL
-- )
-- INSERT INTO wrk.users (first_name, last_name)
-- SELECT first_name, last_name FROM users_names_not_presented_in_users;

-- ALTER TABLE wrk.users ADD COLUMN


-- user_code ->
--  skyteam -> flights info
--  loyality_.., ->
--    forum_profile -> first_name, last_name
--  pointz ->
--
--
--
--
--
--
--
--
--
--
--
--
--
