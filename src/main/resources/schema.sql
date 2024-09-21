CREATE TABLE movie (
                       movie_id BIGINT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       price DECIMAL(10, 2),
                       genre VARCHAR(100),
                       duration INT NOT NULL,
                       img_url VARCHAR(255),
                       description TEXT
);

CREATE TABLE theater (
                         theater_id BIGINT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         rows INT NOT NULL,
                         seats_in_row INT NOT NULL
);

CREATE TABLE seat (
                      seat_id BIGINT PRIMARY KEY,
                      seat_num INT NOT NULL,
                      row_num INT NOT NULL,
                      theater_id BIGINT,
                      FOREIGN KEY (theater_id) REFERENCES theater(theater_id)
);

CREATE TABLE screening (
                           screening_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                           movie_id BIGINT,
                           theater_id BIGINT,
                           date DATE NOT NULL,
                           time TIME NOT NULL,
                           FOREIGN KEY (movie_id) REFERENCES movie(movie_id),
                           FOREIGN KEY (theater_id) REFERENCES theater(theater_id)
);

CREATE TABLE users (
                       user_id BIGINT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255),
                       is_admin BOOLEAN DEFAULT FALSE
);

CREATE TABLE booking (
                         booking_id BIGINT PRIMARY KEY,
                         booking_time TIMESTAMP NOT NULL,
                         user_id BIGINT,
                         screening_id BIGINT,
                         FOREIGN KEY (user_id) REFERENCES users(user_id),
                         FOREIGN KEY (screening_id) REFERENCES screening(screening_id)
);

CREATE TABLE ticket (
                        ticket_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                        booking_id BIGINT,
                        seat_id BIGINT,
                        screening_id BIGINT,
                        price DECIMAL(10, 2),
                        status VARCHAR(20),
                        FOREIGN KEY (booking_id) REFERENCES booking(booking_id),
                        FOREIGN KEY (seat_id) REFERENCES seat(seat_id),
                        FOREIGN KEY (screening_id) REFERENCES screening(screening_id)
);

