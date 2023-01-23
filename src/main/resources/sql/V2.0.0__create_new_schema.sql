CREATE TABLE "releaseTracker"."public".app_build
(
    app_name       TEXT    NOT NULL,
    build_number   INTEGER NOT NULL,
    author         TEXT,
    commit_message TEXT,
    story          TEXT
)