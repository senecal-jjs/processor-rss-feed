CREATE TABLE IF NOT EXISTS subscription(
    id uuid NOT NULL PRIMARY KEY,
    profile_id uuid NOT NULL,
    channel_id uuid NOT NULL,
    channel_url TEXT NOT NULL,
    category TEXT NOT NULL
);