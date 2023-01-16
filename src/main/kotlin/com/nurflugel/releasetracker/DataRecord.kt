package com.nurflugel.releasetracker

import com.nurflugel.releasetracker.Environment.STAGE
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DataRecord {
  var author: String
  var commitMessage: String?
  var date: LocalDateTime?
  var deployEnvironment: Environment
  var buildNumber: Int
  var appName: String
  var story: String?

  companion object {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH&mm");
  }

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
        Pair(split[0], split[1].trim())
      }
    this.appName = valueMap["app"].toString().replace("cc-", "")
    this.buildNumber = parseBuildNumber(valueMap["build"].toString())
    this.deployEnvironment = parseEnvironment(valueMap["deployed"].toString())
    this.date = parseDate(valueMap["date"].toString())
    this.author = valueMap["author"].toString()
    this.commitMessage = parseCommitMessage(valueMap["commitMessage"].toString())
    this.story = parseStoryFromCommit(valueMap["commitMessage"].toString())
  }

  private fun parseCommitMessage(text: String): String {
    return text.replace('\'', '"')
  }

  private fun parseBuildNumber(possibleValue: String): Int {
    try {
      return when {
        possibleValue.isBlank() -> 0
        else                    -> Integer.parseInt(possibleValue)
      }
    } catch (e: Exception) {
      TODO("Not yet implemented")
    }
  }

  private fun parseEnvironment(possibleValue: String): Environment {
    val envText = possibleValue
      .substringAfter("wi-")
      .uppercase(Locale.getDefault())
    try {
      return when {
        envText.isEmpty() -> STAGE
        envText == "NULL" -> STAGE
        else              -> Environment.valueOf(envText)
      }
    } catch (e: Exception) {
      TODO("Not yet implemented")
    }
  }

  private fun parseDate(possibleDate: String): LocalDateTime? {
    return try {
      LocalDateTime.parse(possibleDate, formatter)
    } catch (e: Exception) {
      null
    }
  }

  private fun parseStoryFromCommit(commitMessage: String): String? {
    if (commitMessage.contains("PME")) {
      val trimIt = commitMessage.substringBefore("PME")
      return commitMessage
        .substringAfter(trimIt)
        .substringBefore(" ")
        .substringBefore("_")
    }
    return null
  }
}
