-- Insert movies if they don't already exist

--need to remove id from the insert statements

INSERT INTO movie (id, title, genre, price, duration, img_url, description)
SELECT 1, 'Inception', 'Sci-Fi', 10.99, 148, 'inception.jpg', 'A mind-bending thriller.'
WHERE NOT EXISTS (SELECT 1 FROM movie WHERE id = 1);

INSERT INTO movie (id, title, genre, price, duration, img_url, description)
SELECT 2, 'The Dark Knight', 'Action', 12.00, 152, 'dark_knight.jpg', 'A gripping tale of Gotham’s hero.'
WHERE NOT EXISTS (SELECT 1 FROM movie WHERE id = 2);

INSERT INTO movie (id, title, genre, price, duration, img_url, description)
SELECT 3, 'Interstellar', 'Sci-Fi', 14.50, 169, 'interstellar.jpg', 'Exploring space and time.'
WHERE NOT EXISTS (SELECT 1 FROM movie WHERE id = 3);

-- Insert theaters if they don't already exist
INSERT INTO theater (id, name, rows, seats_in_row, capacity)
SELECT 1, 'Theater 1', 10, 20, 200
WHERE NOT EXISTS (SELECT 1 FROM theater WHERE id = 1);

INSERT INTO theater (id, name, rows, seats_in_row, capacity)
SELECT 2, 'Theater 2', 8, 15, 120
WHERE NOT EXISTS (SELECT 1 FROM theater WHERE id = 2);

INSERT INTO theater (id, name, rows, seats_in_row, capacity)
SELECT 3, 'Theater 3', 12, 25, 300
WHERE NOT EXISTS (SELECT 1 FROM theater WHERE id = 3);

-- Insert seats if they don't already exist
INSERT INTO seat (id, seat_num, row_num, theater_id)
SELECT 1, 1, 1, 1
WHERE NOT EXISTS (SELECT 1 FROM seat WHERE id = 1);

INSERT INTO seat (id, seat_num, row_num, theater_id)
SELECT 2, 2, 1, 1
WHERE NOT EXISTS (SELECT 1 FROM seat WHERE id = 2);

INSERT INTO seat (id, seat_num, row_num, theater_id)
SELECT 3, 3, 2, 1
WHERE NOT EXISTS (SELECT 1 FROM seat WHERE id = 3);

-- Insert screenings if they don't already exist
INSERT INTO screening (id, movie_id, theater_id, date, time)
SELECT 1, 1, 1, '2023-09-25', '18:00:00'
WHERE NOT EXISTS (SELECT 1 FROM screening WHERE id = 1);

INSERT INTO screening (id, movie_id, theater_id, date, time)
SELECT 2, 2, 1, '2023-09-26', '20:00:00'
WHERE NOT EXISTS (SELECT 1 FROM screening WHERE id = 2);

INSERT INTO screening (id, movie_id, theater_id, date, time)
SELECT 3, 3, 2, '2023-09-27', '19:30:00'
WHERE NOT EXISTS (SELECT 1 FROM screening WHERE id = 3);

-- Insert users if they don't already exist
INSERT INTO users (id, username, password, email, is_admin)
SELECT 1, 'john_doe', 'password123', 'john@example.com', FALSE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE id = 1);

INSERT INTO users (id, username, password, email, is_admin)
SELECT 2, 'jane_doe', 'password456', 'jane@example.com', FALSE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE id = 2);

INSERT INTO users (id, username, password, email, is_admin)
SELECT 3, 'admin_user', 'adminpassword', 'admin@example.com', TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE id = 3);

-- Insert bookings if they don't already exist
INSERT INTO booking (id, booking_time, user_id, screening_id)
SELECT 1, '2023-09-25 17:30:00', 1, 1
WHERE NOT EXISTS (SELECT 1 FROM booking WHERE id = 1);

INSERT INTO booking (id, booking_time, user_id, screening_id)
SELECT 2, '2023-09-26 19:30:00', 2, 2
WHERE NOT EXISTS (SELECT 1 FROM booking WHERE id = 2);

INSERT INTO booking (id, booking_time, user_id, screening_id)
SELECT 3, '2023-09-27 19:00:00', 3, 3
WHERE NOT EXISTS (SELECT 1 FROM booking WHERE id = 3);

-- Insert tickets if they don't already exist
INSERT INTO ticket (id, booking_id, seat_id, screening_id, price, status)
SELECT 1, 1, 1, 1, 10.00, 'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM ticket WHERE id = 1);

INSERT INTO ticket (id, booking_id, seat_id, screening_id, price, status)
SELECT 2, 2, 2, 2, 12.00, 'EXPIRED'
WHERE NOT EXISTS (SELECT 1 FROM ticket WHERE id = 2);

INSERT INTO ticket (id, booking_id, seat_id, screening_id, price, status)
SELECT 3, 3, 3, 3, 14.00, 'CANCELLED'
WHERE NOT EXISTS (SELECT 1 FROM ticket WHERE id = 3);
