CREATE TABLE wrk.forum_profiles_normalized (
    id uuid primary key references forum_profiles(id),
    first_name varchar,
    last_name varchar
);
CREATE INDEX forum_profiles_normalized_last_name_idx ON wrk.forum_profiles_normalized(last_name);