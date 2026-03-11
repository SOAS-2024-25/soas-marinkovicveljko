INSERT INTO bank_account (id, email, eur, usd, gbp, chf, rsd)
VALUES
    (1, 'user@uns.ac.rs', 100.00, 50.00, 20.00, 0.00, 30000.00);

ALTER SEQUENCE bank_acc_seq RESTART WITH 2;