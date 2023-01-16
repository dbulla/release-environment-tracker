package com.nurflugel.releasetracker

import java.time.LocalDateTime
import java.util.*

class DataRecord {
  var author: String
  var commitMessage: String
  var date: LocalDateTime?
  var deployEnvironment: Environment
  var buildNumber: Int
  var appName: String
  var story: String?

  constructor(appName: String, buildNumber: Int, deployEnvironment: Environment, date: LocalDateTime?, commitMessage: String, author: String, story: String?) {
    this.appName = appName
    this.buildNumber = buildNumber
    this.deployEnvironment = deployEnvironment
    this.date = date
    this.commitMessage = commitMessage
    this.author = author
    this.story = story
  }

  constructor(text: String) {
    val valueMap = text.split("### ")
      .associate {
        val split = it.split(":")
        Pair(split[0], split[1])
      }
    this.appName = valueMap["app"].toString().trim()
    val ssss: String? = valueMap["build"]

    try {
      this.buildNumber = when {
        ssss.isNullOrBlank() -> 0
        else                 -> Integer.parseInt(ssss.trim())

      }
    } catch (e: Exception) {
      TODO("Not yet implemented")
    }
    val toString = valueMap["deployed"].toString().trim()
    val env = toString.substringAfter("wi-").uppercase(Locale.getDefault())
    try {
      this.deployEnvironment = when {
        env.isEmpty() -> Environment.STAGE
        env == "NULL" -> Environment.STAGE
        else          -> Environment.valueOf(env)
      }
    } catch (e: Exception) {
      TODO("Not yet implemented")
    }
    this.date = null // LocalDateTime.now() //     valueMap["date"]
    this.commitMessage =
      valueMap["commitMessage"].toString().replace('\'', '"').trim()  //todo need to deal with SQL injection - when a message has 'main' or something in quotes....
    this.author = valueMap["author"].toString().trim()
    this.story = parseStoryFromCommit(commitMessage)
  }

  private fun parseStoryFromCommit(commitMessage: String): String? {
    if (commitMessage.contains("PME")) {
      val trimmit = commitMessage.substringBefore("PME")
      val after = commitMessage.substringAfter(trimmit)
      val storyNumber = after.substringBefore(" ").substringBefore("_")
      return storyNumber
    }
    return null
  }

}
