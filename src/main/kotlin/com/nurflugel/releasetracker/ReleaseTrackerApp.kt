package com.nurflugel.releasetracker

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.Banner
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootApplication
class ReleaseTrackerApp(
  @Autowired val jdbcTemplate: JdbcTemplate,
) : CommandLineRunner {

  companion object {
    @JvmStatic
    fun main(vararg args: String) {
      val flag = if (args.isEmpty()) {
        "-both"
      } else {
        args[0]
      }

      SpringApplicationBuilder(ReleaseTrackerApp::class.java)
        .web(WebApplicationType.NONE)
        .headless(false)
        .bannerMode(Banner.Mode.OFF)
        .run(flag)
    }
  }

  override fun run(vararg args: String) {
    val flag: String = args[0]
    when {
      flag.substring(0, 2) == "-d" -> DisplayApp().run(jdbcTemplate)
      flag.substring(0, 2) == "-p" -> ParserApp().run(jdbcTemplate)
      flag.substring(0, 2) == "-b" -> {
        ParserApp().run(jdbcTemplate)
        DisplayApp().run(jdbcTemplate)
      }
    }
  }

}
