CREATE TABLE IF NOT EXISTS deploys
(
    app_name       varchar(60) NOT NULL,
    build_number   numeric,
    environment    varchar(10) not null,
    author         varchar(60) NOT NULL,
    commit_message varchar,
    deploy_date    timestamptz DEFAULT now(),
    story          varchar(100),
    created_at     timestamptz DEFAULT now(),
    updated_at     timestamptz DEFAULT now(),
    PRIMARY KEY (app_name, environment, build_number)
);
