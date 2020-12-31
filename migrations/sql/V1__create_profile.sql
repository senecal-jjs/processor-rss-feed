CREATE TABLE IF NOT EXISTS profile(
    id UUID NOT NULL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(300) NOT NULL,
    authority JSONB
);