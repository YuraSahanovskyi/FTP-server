CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    working_dir VARCHAR(255) NOT NULL,
    speed_limit INTEGER
);

CREATE TABLE permissions (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    path VARCHAR(255) NOT NULL,
    can_read BOOLEAN DEFAULT FALSE,
    can_write BOOLEAN DEFAULT FALSE,
    can_execute BOOLEAN DEFAULT FALSE
);

INSERT INTO users (username, password, working_dir, speed_limit)
VALUES('ftp_user', 'ftp_password', '/app/data/ftp_user', 1000);

INSERT INTO permissions (user_id, path, can_read, can_write, can_execute)
VALUES ((SELECT id FROM users WHERE username = 'ftp_user'), '/app/data/ftp_user', TRUE, TRUE, FALSE);
