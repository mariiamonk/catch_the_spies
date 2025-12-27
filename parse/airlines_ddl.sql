create table boarding_pass
(
    id          uuid default gen_random_uuid() not null
        primary key,
    name        varchar,
    flight      varchar,
    "from"      varchar,
    "to"        varchar,
    from_code   varchar,
    to_code     varchar,
    date        date,
    time        time,
    operated_by varchar,
    seat        varchar,
    pnr         varchar,
    etiket      varchar,
    sequence    integer
);

alter table boarding_pass
    owner to airlines;

create table forum_profiles
(
    id         uuid default gen_random_uuid() not null
        primary key,
    nickname   varchar                        not null,
    sex        varchar,
    first_name varchar,
    last_name  varchar
);

alter table forum_profiles
    owner to airlines;

create table airport
(
    code    varchar not null
        primary key,
    city    varchar,
    country varchar
);

alter table airport
    owner to airlines;

create table forum_profiles_flights
(
    id         uuid default gen_random_uuid() not null
        primary key,
    profile_id uuid                           not null
        references forum_profiles,
    date       date                           not null,
    depature   varchar                        not null
        references airport,
    arrival    varchar                        not null
        references airport
);

alter table forum_profiles_flights
    owner to airlines;

create table loyality_program
(
    id         uuid default gen_random_uuid() not null
        primary key,
    profile_id uuid                           not null
        references forum_profiles,
    status     varchar                        not null,
    program    varchar                        not null,
    type       varchar                        not null
);

alter table loyality_program
    owner to airlines;

create table pointz_users
(
    id         uuid default gen_random_uuid() not null
        primary key,
    uid        bigint                         not null,
    first_name varchar                        not null,
    last_name  varchar                        not null
);

alter table pointz_users
    owner to airlines;

create table pointz_activities
(
    id            uuid default gen_random_uuid() not null
        primary key,
    user_id       uuid                           not null
        references pointz_users,
    bonus_program varchar                        not null,
    number        varchar                        not null,
    code          varchar                        not null,
    date          date                           not null,
    departure     varchar                        not null
        references airport,
    arrival       varchar                        not null
        references airport,
    fare          varchar                        not null
);

alter table pointz_activities
    owner to airlines;

create table skyteam_exchange
(
    id          uuid default gen_random_uuid() not null
        primary key,
    user_code   varchar                        not null,
    user_fare   varchar                        not null,
    user_cls    varchar                        not null,
    flight_code varchar,
    date        date                           not null,
    departure   varchar                        not null
        references airport,
    arrival     varchar                        not null
        references airport,
    status      varchar
);

alter table skyteam_exchange
    owner to airlines;

create table databasechangeloglock
(
    id          integer not null
        primary key,
    locked      boolean not null,
    lockgranted timestamp,
    lockedby    varchar(255)
);

alter table databasechangeloglock
    owner to airlines;

create table databasechangelog
(
    id            varchar(255) not null,
    author        varchar(255) not null,
    filename      varchar(255) not null,
    dateexecuted  timestamp    not null,
    orderexecuted integer      not null,
    exectype      varchar(10)  not null,
    md5sum        varchar(35),
    description   varchar(255),
    comments      varchar(255),
    tag           varchar(255),
    liquibase     varchar(20),
    contexts      varchar(255),
    labels        varchar(255),
    deployment_id varchar(10)
);

alter table databasechangelog
    owner to airlines;

create table sirena_export_users
(
    id          uuid default gen_random_uuid() not null
        primary key,
    first_name  varchar,
    last_name   varchar,
    second_name varchar,
    document    varchar,
    pax_name    varchar,
    birth_date  date
);

alter table sirena_export_users
    owner to airlines;

create table sirena_export
(
    id                  uuid default gen_random_uuid() not null
        primary key,
    pax_name            varchar,
    pax_birth_data      date,
    departure_date      date,
    departure_time      time,
    arrival_date        date,
    arrival_time        time,
    flight              varchar,
    code_sh             boolean,
    "from"              varchar,
    dest                varchar,
    code                varchar,
    eticket             varchar,
    travel_doc          varchar,
    seat                varchar,
    meal                varchar,
    trv_cls             varchar,
    fare                varchar,
    baggage             varchar,
    pax_additional_info varchar,
    agent_info          varchar,
    user_id             uuid
        references sirena_export_users
);

alter table sirena_export
    owner to airlines;

create table boarding_data_users
(
    id          uuid default gen_random_uuid() not null
        primary key,
    first_name  varchar,
    last_name   varchar,
    second_name varchar,
    sex         varchar,
    document    varchar,
    birth_date  varchar
);

alter table boarding_data_users
    owner to airlines;

create table boarding_data
(
    id                    uuid default gen_random_uuid() not null
        primary key,
    passenger_first_name  varchar                        not null,
    passenger_second_name varchar                        not null,
    passenger_last_name   varchar                        not null,
    passenger_sex         varchar,
    passenger_birth_date  date,
    passenger_document    varchar,
    booking_code          varchar,
    ticket_number         varchar,
    baggage               varchar,
    flight_date           date,
    flight_time           time,
    flight_number         varchar,
    code_share            varchar,
    destination           varchar,
    user_id               uuid
        references boarding_data_users
);

alter table boarding_data
    owner to airlines;

create index boarding_data_document_idx
    on boarding_data (passenger_document);

create index boarding_data_user_id_idx
    on boarding_data (user_id);


