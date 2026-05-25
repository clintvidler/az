CREATE TABLE task
(
    id         BIGINT PRIMARY KEY,
    title      TEXT      NOT NULL,
    completed  BOOLEAN,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    user_id    BIGINT    NOT NULL
);

CREATE TABLE users
(
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(500) NOT NULL,
    enabled  BOOLEAN      NOT NULL
);

CREATE TABLE authorities
(
    username  VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT fk_authorities_users
        FOREIGN KEY (username) REFERENCES users (username)
);

-- Configure the schemas for prod, not yet in local dev. This will enable quicker iteration driven by the jpa classes.