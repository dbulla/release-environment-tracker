/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package com.nurflugel.releasetracker

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.time.LocalDateTime

class DisplayApp {

  fun run(jdbcTemplate: JdbcTemplate, allResults: Boolean) {
    val records = loadData(jdbcTemplate)
    val filteredData = filterData(records, allResults)
    showData(filteredData)
  }

  private fun showData(filteredData: List<DataRecord>) {
    var oldAppName: String? = null
    println(
      "\n\n\nApp".padEnd(45) + "   Environment".padEnd(30) + "   Version".padEnd(18) + "Story".padEnd(13) + "Commit Message".padEnd(67) +
      "Build #".padEnd(18) + "Date".padEnd(15)
    )
    println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------")
    for (datum in filteredData) {
      val appName = datum.appName.padEnd(45)
      val deployEnvironment = datum.deployEnvironment.toString().padEnd(30)
      val buildNumber = String.format("%d", datum.buildNumber).padEnd(15)
      val commitMessage = suppressNullText(datum.commitMessage)
        .take(60)
        .padEnd(70)
      val version = suppressNullText(datum.version).padEnd(15)
      val story = suppressNullText(datum.story)
        .take(20)
        .padEnd(10)
      val date = "   " + datum.date.toString().padEnd(15)

      val line = appName + deployEnvironment + version + story + commitMessage + buildNumber + date
      if (oldAppName != appName && !appName.contains("shared")) println()
      println(line)
      oldAppName = appName
    }
  }

  private fun suppressNullText(text: String?): String = if (text == null || text == "null") "" else text

  // take the raw data and:
  //   show the latest build for each app, and what envs it's at - show the full record
  private fun filterData(data: List<DataRecord>, allResults: Boolean): List<DataRecord> {
    return if (allResults)
      data
    else {
      val results = mutableListOf<DataRecord>()
      val applicationDeployMap: Map<String, List<DataRecord>> = data.groupBy { it.appName }
      val keys = applicationDeployMap.keys.sorted()
      for (key in keys) {
        val appDeploys: List<DataRecord> = applicationDeployMap[key]!!
        // filter this so only the highest build number remains - note that we might also want to filter on commit message...
        val maxBuild = appDeploys.map { it.buildNumber }.max()
        val latestEnvsForBuild: List<DataRecord> = appDeploys.filter { it.buildNumber == maxBuild }
        results.addAll(latestEnvsForBuild)
      }
      results
    } // no filtering
  }

  private fun loadData(jdbcTemplate: JdbcTemplate): List<DataRecord> {
    val sql: String = ("""
              SELECT app_name, build_number, author, commit_message, deploy_date, environment, story, version 
              FROM deploys
                """).trimIndent();
    //Declare rowMapper to map DB records to collection of Beer entities:
    val rowMapper: RowMapper<DataRecord> = RowMapper<DataRecord> { resultSet: ResultSet, rowIndex: Int ->
      val appName = resultSet.getString("app_name")
      val buildNumber = resultSet.getInt("build_number")
      val environmentAsString = resultSet.getString("environment")
      val environment = Environment.valueOf(environmentAsString)
      val timestamp = resultSet.getTimestamp("deploy_date")
      val date: LocalDateTime = timestamp.toLocalDateTime()
      val commitMessage = resultSet.getString("commit_message")
      val author = resultSet.getString("commit_message")
      val story = resultSet.getString("story")
      val version = resultSet.getString("version")
      val dataRecord = DataRecord(appName, buildNumber, environment, date, commitMessage, author, story, version)
      dataRecord
    }

    return jdbcTemplate.query(sql, rowMapper)
  }

}
