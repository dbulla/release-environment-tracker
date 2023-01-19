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

  override fun run(vararg args: String?) {
    val flag = args[0]
    when {
      flag.equals("-display") -> PresentationApp().run(jdbcTemplate)
      flag.equals("-parse")   -> SlackParserApp().run(jdbcTemplate)
      flag.equals("-both")    -> {
        SlackParserApp().run(jdbcTemplate)
        PresentationApp().run(jdbcTemplate)
      }
    }
  }

}
