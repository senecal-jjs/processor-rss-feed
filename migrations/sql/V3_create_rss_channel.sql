CREATE TABLE IF NOT EXISTS rss_channel(
    id uuid NOT NULL PRIMARY KEY,
    title TEXT,
    site_url TEXT,
    channel_url TEXT NOT NULL,
    channel_desc TEXT,
    topics JSONB
);