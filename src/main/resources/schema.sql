-- Movie table schema
CREATE TABLE IF NOT EXISTS movie (
                                     id BIGSERIAL PRIMARY KEY,
                                     title VARCHAR(255) NOT NULL,
                                     price DECIMAL(10, 2),
                                     genre VARCHAR(100),
                                     duration INT NOT NULL,
                                     img_url VARCHAR(255),
                                     description TEXT
);

-- Theater table schema
CREATE TABLE IF NOT EXISTS theater (
                                       id BIGSERIAL PRIMARY KEY,
                                       name VARCHAR(100) NOT NULL,
                                       rows INT NOT NULL,
                                       seats_in_row INT NOT NULL,
                                       capacity INT NOT NULL
);

-- Seat table schema
CREATE TABLE IF NOT EXISTS seat (
                                    id BIGSERIAL PRIMARY KEY,
                                    seat_num INT NOT NULL,
                                    row_num INT NOT NULL,
                                    theater_id BIGSERIAL REFERENCES theater(id)
);

-- Screening table schema
CREATE TABLE IF NOT EXISTS screening (
                                         id BIGSERIAL PRIMARY KEY,
                                         movie_id BIGSERIAL REFERENCES movie(id),
                                         theater_id BIGSERIAL REFERENCES theater(id),
                                         date DATE NOT NULL,
                                         time TIME NOT NULL
);

-- User table schema
CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     username VARCHAR(100) NOT NULL,
                                     password VARCHAR(255) NOT NULL,
                                     email VARCHAR(255),
                                     is_admin BOOLEAN NOT NULL
);

-- Booking table schema
CREATE TABLE IF NOT EXISTS booking (
                                       id BIGSERIAL PRIMARY KEY,
                                       booking_time TIMESTAMP NOT NULL,
                                       user_id BIGSERIAL REFERENCES users(id),
                                       screening_id BIGSERIAL REFERENCES screening(id)
);

-- Ticket table schema
CREATE TABLE IF NOT EXISTS ticket (
                                      id BIGSERIAL PRIMARY KEY,
                                      booking_id BIGSERIAL REFERENCES booking(id),
                                      seat_id BIGSERIAL REFERENCES seat(id),
                                      screening_id BIGSERIAL REFERENCES screening(id),
                                      price DECIMAL(10, 2) NOT NULL,
                                      status VARCHAR(50)
);
