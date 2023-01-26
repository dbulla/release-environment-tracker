-- CREATE TABLE "releaseTracker"."public".app_build
-- (
--     app_name       TEXT    NOT NULL,
--     build_number   INTEGER NOT NULL,
--     author         TEXT,
--     commit_message TEXT,
--     story          TEXT
-- )

alter table deploys
    ADD column commit_hash text;
