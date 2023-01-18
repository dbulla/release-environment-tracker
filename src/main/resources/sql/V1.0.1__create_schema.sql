CREATE TABLE IF NOT EXISTS deploys
(
    app_name       varchar NOT NULL,
    build_number   numeric,
    environment    varchar not null,
    author         varchar NOT NULL,
    commit_message varchar,
    deploy_date    timestamptz DEFAULT now(),
    story          varchar,
    version        varchar,
    created_at     timestamptz DEFAULT now(),
    updated_at     timestamptz DEFAULT now(),
    PRIMARY KEY (app_name, environment, build_number)
);
