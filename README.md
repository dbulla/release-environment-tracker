# release-environment-tracker

Util tool to scrape env deploys from slack/email and track what was released where and when.

It just reads in lines, expecting a format like this(note the delimiter between the hour and minute:

```
#app:Some app name###### build:46###### deployed: prod###### commitMessage: And now for something completely different### author: dbulla ### date: 2023-01-15 17&26
```

It puts the items into a database that's created in Docker.

## Create the database

`./gradlew composeUp`

## Run the Flyway scripts to create tables, etc

`./gradlew runFlyway`

## Enter data

First, copy the data you want added into the paste buffer. Then, just run
`./gradlew run`

## Make an executable jar

Once you've got things working, use Spring to create an executable jar:
`./gradlew bootJar`
This will be in the `build/libs` dir.