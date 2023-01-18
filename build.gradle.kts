/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/7.6/userguide/building_java_projects.html
 * This project uses @Incubating APIs which are subject to change.
 */
//import org.jooq.meta.jaxb.ForcedType
//import org.jooq.meta.jaxb.Logging
//import org.jooq.meta.jaxb.Property
import org.flywaydb.gradle.task.FlywayMigrateTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

var KOTEST_VERSION = "5.5.4"
val databaseLibrary = "org.postgresql:postgresql:42.5.1"

plugins {
  // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
  kotlin("jvm") version "1.8.0"

  // Apply the application plugin to add support for building a CLI application in Java.
  application

  id("org.springframework.boot") version "3.0.1"
  id("io.spring.dependency-management") version "1.1.0"
  kotlin("plugin.spring") version "1.8.0"

  id("com.github.ben-manes.versions") version "0.44.0"
  id("com.dorongold.task-tree") version "2.1.1"

  id("com.avast.gradle.docker-compose") version "0.16.11"
  id("org.flywaydb.flyway") version "9.11.0"
}

repositories {
  // Use Maven Central for resolving dependencies.
  mavenCentral()
}

java.sourceCompatibility = JavaVersion.VERSION_17

dependencies {
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
  // This dependency is used by the application.
  implementation("com.google.guava:guava:31.1-jre")
  implementation(databaseLibrary)
  implementation("org.springframework.boot:spring-boot-starter")
  implementation("org.springframework.boot:spring-boot-starter-jdbc:3.0.1")

  runtimeOnly("org.postgresql:postgresql")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.kotest:kotest-runner-junit5:$KOTEST_VERSION")
  testImplementation("io.kotest:kotest-assertions-core:$KOTEST_VERSION")
  testImplementation("io.kotest:kotest-property:$KOTEST_VERSION")

}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "17"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

val runFlyway by tasks.registering(FlywayMigrateTask::class) {
  dependsOn("processResources")
  description = "Run the Flyway migration"
  url = "jdbc:postgresql://localhost:5432/releaseTracker"
  user = "me"
  password = "noPassword"
  schemas = arrayOf("public")
  locations = arrayOf("filesystem:${project.buildDir}/resources/main/sql")
}

tasks.getByName<BootJar>("bootJar") {
  mainClass.set("com.nurflugel.releasetracker.SlackParserApp")
  launchScript()
}

testing {
  suites {
    // Configure the built-in test suite
    val test by getting(JvmTestSuite::class) {
      // Use KotlinTest test framework
      useKotlinTest("1.8.0")

      dependencies {
        // Use newer version of JUnit Engine for Kotlin Test
        implementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
      }
    }
  }
}

application {
  // Define the main class for the application.
  mainClass.set("com.nurflugel.releasetracker.SlackParserApp")
  group = "com.nurflugel"
  version = "0.0.1-SNAPSHOT"
}
