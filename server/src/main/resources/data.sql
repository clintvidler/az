INSERT INTO task (id, title, user_id) VALUES (1, 'Rover', 1);
INSERT INTO task (id, title, user_id) VALUES (2, 'Fido', 1);
INSERT INTO task (id, title, user_id) VALUES (3, 'Sam', 1);

INSERT INTO users (username, password, enabled)
VALUES ('super','{noop}PASSWORD_DISABLED_FOR_PASSKEYS_' || RANDOM_UUID(),true);

INSERT INTO authorities (username, authority)
VALUES ('super', 'ROLE_USER');
