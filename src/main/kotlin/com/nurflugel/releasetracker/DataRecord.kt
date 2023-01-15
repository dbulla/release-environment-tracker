package com.nurflugel.releasetracker

import java.time.LocalDateTime
import java.util.*

class DataRecord {
  var author: String
  var commitMessage: String
  var date: LocalDateTime?
  var deployEnvironment: Environment
  var buildNumber: String
  var appName: String
  var story: String?

  constructor(appName: String, buildNumber: String, deployEnvironment: Environment, date: LocalDateTime?, commitMessage: String, author: String, story: String?) {
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
    this.buildNumber = valueMap["build"].toString().trim()
    val toString = valueMap["deployed"].toString().trim()
    val env = toString.substringAfter("wi-").uppercase(Locale.getDefault())
    try {
      this.deployEnvironment = when {
        env.isEmpty()         -> Environment.STAGE
        env == "NULL"         -> Environment.STAGE
        env == "STAGE - NULL" -> Environment.STAGE
        else                  -> Environment.valueOf(env)
      }
    } catch (e: Exception) {
      TODO("Not yet implemented")
    }
    this.date = null // LocalDateTime.now() //     valueMap["date"]
    this.commitMessage = valueMap["commitMessage"].toString().trim()
    this.author = valueMap["author"].toString().trim()
    this.story = parseStoryFromCommit(commitMessage)
  }

  private fun parseStoryFromCommit(commitMessage: String): String? {
    if (commitMessage.contains("PME")) {
      val trimmit = commitMessage.substringBefore("PME")
      val after = commitMessage.substringAfter(trimmit)
      val storyNumber = after.substringBefore(" ")
      return storyNumber
    }
    return null
  }

}
