CREATE TABLE IF NOT EXISTS tvg_info
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    tvg_id TEXT,
    tvg_name TEXT,
    tvg_logo TEXT,
    group_title TEXT,
    title TEXT,
    url TEXT,
    status TEXT,
    cost_time TEXT,
    last_check_time TEXT,
    video_width INTEGER,
    video_height INTEGER,
    video_codec TEXT,
    video_fps INTEGER,
    audio_codec TEXT,
    audio_sample_rate INTEGER
);