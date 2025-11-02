CREATE TABLE player (
    id INTEGER GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY ,
    name TEXT NOT NULL,
    gold INTEGER NOT NULL DEFAULT 0 CHECK (gold >= 0),
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now()
);

CREATE TABLE skill (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL ,
    player_id INTEGER REFERENCES player(id) ON DELETE CASCADE ,
    name TEXT NOT NULL,
    xp INTEGER NOT NULL DEFAULT 0 CHECK ( xp >= 0 ),
    level INTEGER NOT NULL DEFAULT 1 CHECK (level >= 1)
);

CREATE TABLE inventory (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL ,
    player_id INTEGER REFERENCES player(id) ON DELETE CASCADE ,
    item_name TEXT NOT NULL ,
    count INTEGER NOT NULL DEFAULT 0 CHECK ( count >= 0 )
);

CREATE UNIQUE INDEX idx_player_name ON player(name);
CREATE UNIQUE INDEX idx_unique_skill_per_player ON skill(player_id, name);
CREATE UNIQUE INDEX idx_unique_item_per_player ON inventory(player_id, item_name);

