
CREATE TABLE wrk.user_code_agg (
    user_code varchar primary key,
    forum_profile_id uuid references forum_profiles (id), --> forum_profiles(first_name, last_name, sex)
    pointz_user_id uuid references pointz_users (id),

    first_name varchar,
    last_name varchar,
    user_id uuid references users (id)
);

