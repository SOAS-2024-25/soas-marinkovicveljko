INSERT INTO user_model (id, email, password, role)
VALUES
    (1, 'owner@uns.ac.rs', 'ownerPassword', 'OWNER'),
    (2, 'admin@uns.ac.rs', 'adminPassword', 'ADMIN'),
    (3, 'user@uns.ac.rs', 'userPassword', 'USER');

ALTER SEQUENCE user_seq RESTART WITH 4;