CREATE TABLE IF NOT EXISTS deploys
(
    app_name       varchar(60) NOT NULL,
    author         varchar(60) NOT NULL,
    commit_message varchar     not null,
    deploy_date    timestamptz DEFAULT now(),
    environment    varchar(4)  not null,
    story          varchar(100),
    created_at     timestamptz DEFAULT now(),
    updated_at     timestamptz DEFAULT now(),
    PRIMARY KEY (app_name, environment, commit_message)
);
