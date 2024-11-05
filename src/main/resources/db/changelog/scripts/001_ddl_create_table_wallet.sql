CREATE TABLE IF NOT EXISTS wallet
(
wallet_id UUID PRIMARY KEY,
amount decimal NOT NULL CHECK (amount >= 0)
);
