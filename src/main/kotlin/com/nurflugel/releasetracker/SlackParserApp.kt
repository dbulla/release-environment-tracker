/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package com.nurflugel.releasetracker

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.Banner
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor.stringFlavor
import java.lang.management.ManagementFactory
import java.time.LocalDateTime

@SpringBootApplication
//class SlackParserApp(@Autowired val jdbcTemplate: JdbcTemplate) : CommandLineRunner {
class SlackParserApp(@Autowired val jdbcTemplate: JdbcTemplate) : CommandLineRunner {
  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      SpringApplicationBuilder(SlackParserApp::class.java)
        .web(WebApplicationType.NONE)
        .headless(false)
        .bannerMode(Banner.Mode.OFF)
        .run()
    }
  }

  override fun run(vararg args: String?) {

    val runtimeMxBean = ManagementFactory.getRuntimeMXBean()
    val jvmArguments = runtimeMxBean.inputArguments.filter { it.startsWith("-X") }

    if (jvmArguments.isNotEmpty()) println("JVM arguments: $jvmArguments")
    val textToProcess = getClipboardContents()
    if (textToProcess != null) {
      val newData: List<DataRecord> = parseData(textToProcess)
      saveData(newData)
    } else {
      println("No data found in buffer to process")
    }
  }

  private fun saveData(newData: List<DataRecord>) {
    println("newData size: ${newData.size}")
    for (datum in newData) {
      var date = datum.date
      if (date == null) {
        date = LocalDateTime.now()
      }
      //      println("::::::::::::datum = ${datum.appName} ${datum.author} ${datum.commitMessage} ${datum.deployEnvironment} ${datum.story} ${datum.date} ")
      val sql = ("""
              INSERT INTO deploys (app_name, build_number, author, commit_message, deploy_date, environment, story, version)
              VALUES ('${datum.appName}','${datum.buildNumber}', '${datum.author}','${datum.commitMessage}','$date','${datum.deployEnvironment}','${datum.story}','${datum.version}')
              ON CONFLICT (app_name, build_number, environment) DO UPDATE
                 SET deploy_date = excluded.deploy_date,
                     commit_message = excluded.commit_message,
                     story = excluded.story;
                """).trimIndent();
      try {
        jdbcTemplate.update(sql)
      } catch (e: Exception) {
        println("e.message = ${e.message}")
      }
    }
  }

  /** Take the text copy/pasted text and parse the data we want out of it */
  private fun parseData(textToProcess: String): List<DataRecord> {
    //    println(textToProcess)

    val lines = textToProcess.split('\n')
    return lines
      .filter { it.startsWith("app:") }
      .filter { !it.startsWith("app: dibble") }
      .map { DataRecord(it) }
  }

  private fun getClipboardContents(): String? {
    println("getClipboardContents()")
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    val contents = clipboard.getContents(null)
    val hasTransferableText = contents != null && contents.isDataFlavorSupported(stringFlavor)
    if (hasTransferableText) {
      try {
        return contents!!.getTransferData(stringFlavor) as String
      } catch (e: Exception) {
        println(e)
        e.printStackTrace()
      }
    }
    return null
  }
}
