# release-environment-tracker

Util tool to scrape env deploys from slack/email and track what was released where and when.

It just reads in lines, expecting a format like this(note the delimiter between the hour and minute:

```
#app:Some app name###### build:46###### deployed: prod###### commitMessage: And now for something completely different### author: dbulla ### date: 2023-01-15 17&26
```

It puts the items into a database that's created in Docker, and the data files are persisted in the `postgres-data`
directory,
so it's there again when you burn down the Docker image.

## Create the database via Docker

`./gradlew composeUp`

## Run the Flyway scripts to create tables, etc.

`./gradlew runFlyway`

## Build the bootable jar for fast execution (no compiling, etc)

Once you've got things working, use Spring to create an executable jar:
`./gradlew bootJar`
This will be in the `build/libs` dir.

## Enter data - runSlackParser

### Gradle

First, copy the data you want added into the paste buffer. Then, just run

`./gradlew runSlackParser`

This copies the data from the paste buffer into the database.

### Command line

If you've previously created a bootable jar, you can also run it like so:

`./build/libs/release-environment-tracker-0.0.1-SNAPSHOT.jar -parse`

## Data Presentation

### Database

Ideally, I'd like to see the data in a report that:

- grouped by app
- Only the latest build number
- All environments that build number was deployed to

This query does that:

```postgresql
select deploys.app_name, deploys.environment, max(deploys.build_number) as LatestBuild
from public.deploys
where app_name like '%%'
group by deploys.app_name, deploys.environment-- only the most recent build 
order by app_name;
```

Want to see pretty much everything?

```postgresql
select *
from public.deploys
where app_name like '%shared%'
order by app_name, build_number;

```

Included in the data is the commit message, story, author, date, etc.

### Gradle

Run

`./gradlew runPresentation`

to run the built-in presentation of the data. This presents the data:

- Grouped by app
- Only the latest build number
- All environments that build number was deployed to

Included in the data is the commit message, story, author, date, etc.

### Command line

If you've previously created a bootable jar, you can also run it like so:
`build/libs/release-environment-tracker-0.0.1-SNAPSHOT.jar -display`



