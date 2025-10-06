CREATE SCHEMA IF NOT EXISTS characters;

CREATE TABLE IF NOT exists characters.player(
    character_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    profile_id UUID REFERENCES account.profile ( profile_id ),
    character_name VARCHAR(255) NOT NULL,

    -- ability scores
    strength INTEGER NOT NULL,
    dexterity INTEGER NOT NULL,
    constitution INTEGER NOT NULL,
    intelligence INTEGER NOT NULL,
    wisdom INTEGER NOT NULL,
    charisma INTEGER NOT NULL,

    -- derived statistics
    hit_points INTEGER NOT NULL,
    armor_class INTEGER NOT NULL
);
