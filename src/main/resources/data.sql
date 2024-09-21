
INSERT INTO movie (movie_id, title, genre, price, duration, img_url, description) VALUES
                                                                                      (1, 'Inception', 'Sci-Fi',10.99, 148, 'inception.jpg', 'A mind-bending thriller.'),
                                                                                      (2, 'The Dark Knight', 'Action',12.00, 152, 'dark_knight.jpg', 'A gripping tale of Gotham’s hero.'),
                                                                                      (3, 'Interstellar', 'Sci-Fi', 14.50,169, 'interstellar.jpg', 'Exploring space and time.');


INSERT INTO theater (theater_id, name, rows, seats_in_row) VALUES
                                                               (1, 'Theater 1', 10, 20),
                                                               (2, 'Theater 2', 8, 15);


INSERT INTO seat (seat_id, seat_num, row_num, theater_id) VALUES
                                                              (1, 1, 1, 1),
                                                              (2, 2, 1, 1),
                                                              (3, 3, 2, 1),
                                                              (4, 1, 1, 2),
                                                              (5, 2, 1, 2);


INSERT INTO screening (screening_id, movie_id, theater_id, date, time) VALUES
                                                                           (1, 1, 1, '2023-09-25', '18:00:00'),
                                                                           (2, 2, 1, '2023-09-26', '20:00:00'),
                                                                           (3, 3, 2, '2023-09-27', '19:30:00');


INSERT INTO users (user_id, username, password, email, is_admin) VALUES
                                                                     (1, 'john_doe', 'password123', 'john@example.com', FALSE),
                                                                     (2, 'admin_user', 'adminpassword', 'admin@example.com', TRUE);


INSERT INTO booking (booking_id, booking_time, user_id, screening_id) VALUES
                                                                          (1, '2023-09-25 17:30:00', 1, 1),
                                                                          (2, '2023-09-26 19:30:00', 2, 2),
                                                                          (3, '2023-09-27 19:00:00', 1, 3);

INSERT INTO ticket (booking_id, seat_id, screening_id, price, status) VALUES
    (1, 1, 1, 10.00, 'ACTIVE'),
    (1, 2, 1, 10.00, 'CANCELED'),
    (2, 3, 2, 12.00, 'EXPIRED');
