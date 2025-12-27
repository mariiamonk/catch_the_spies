
CREATE TABLE IF NOT EXISTS airlines.boarding_data (
    id uuid default gen_random_uuid() PRIMARY KEY,
    passenger_first_name VARCHAR NOT NULL,
    passenger_second_name VARCHAR NOT NULL,
    passenger_last_name VARCHAR NOT NULL,
    passenger_sex VARCHAR,
    passenger_birth_date DATE,
    passenger_document VARCHAR,
    booking_code VARCHAR,
    ticket_number VARCHAR,
    baggage VARCHAR,
    flight_date DATE,
    flight_time TIME,
    flight_number VARCHAR,
    code_share VARCHAR,
    destination VARCHAR
)