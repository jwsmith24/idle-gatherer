CREATE TABLE tree
(
    id        INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name      TEXT    NOT NULL,
    xp_value        INTEGER NOT NULL,
    toughness INTEGER NOT NULL DEFAULT 1
);

INSERT INTO tree(name, xp_value, toughness)
VALUES ('oak', 100, 5),
       ('maple', 150, 7);
