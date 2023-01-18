CREATE TABLE IF NOT EXISTS environments
(
    name         text NOT NULL,
    deploy_order numeric,
    PRIMARY KEY (name)
);

insert into environments (name, deploy_order)
values ('DEV', 1);

insert into environments (name, deploy_order)
values ('QA', 2);

insert into environments (name, deploy_order)
values ('STAGE', 3);

insert into environments (name, deploy_order)
values ('PROD', 4);

insert into environments (name, deploy_order)
values ('ARTIFACTORY', 0);

insert into environments (name, deploy_order)
values ('DOCKER', 0);
