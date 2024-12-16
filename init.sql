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
    read BOOLEAN DEFAULT FALSE,
    write BOOLEAN DEFAULT FALSE,
    execute BOOLEAN DEFAULT FALSE
);

INSERT INTO users (username, password, working_dir, speed_limit)
VALUES('ftp_user', 'ftp_password', '/app/data/ftp_user', 1000);

INSERT INTO permissions (user_id, path, read, write, execute)
VALUES ((SELECT id FROM users WHERE username = 'ftp_user'), '/app/data/ftp_user', TRUE, TRUE, FALSE);
