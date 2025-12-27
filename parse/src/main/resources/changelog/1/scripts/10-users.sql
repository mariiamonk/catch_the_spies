
CREATE TABLE IF NOT EXISTS wrk.users (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    person_id uuid,

    pax_name VARCHAR,
    first_name VARCHAR,    --
    last_name VARCHAR,     --
    second_name VARCHAR,   --
    first_name_v2 VARCHAR, --
    last_name_v2 VARCHAR,  --

    sex VARCHAR,
    document VARCHAR,
    birth_date DATE,

    forum_id uuid REFERENCES airlines.forum_profiles(id),
    pointz_id uuid REFERENCES airlines.pointz_users(id),
    sirena_user_id uuid REFERENCES airlines.sirena_export_users(id),
    boarding_data_user_id uuid REFERENCES boarding_data_users(id)
);

ALTER TABLE wrk.users ADD CONSTRAINT user_name_doc_unique UNIQUE(first_name, last_name, document);
ALTER TABLE wrk.users ADD CONSTRAINT user_name_v2_doc_unique UNIQUE(document, first_name_v2, last_name_v2);


CREATE INDEX user_document_idx ON wrk.users(document);

INSERT INTO wrk.users(id) VALUES ('00000000-0000-0000-0000-000000000000'); -- stub user
