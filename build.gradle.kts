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
  //  id("nu.studer.jooq") version "8.1"
}

repositories {
  // Use Maven Central for resolving dependencies.
  mavenCentral()
}

//buildscript {
//  dependencies {
//    classpath("org.jooq:jooq-codegen:3.17.6")
//    classpath("com.h2database:h2:200")
//  }
//}

//val databaseLibrary = "com.h2database:h2:2.1.214"
val databaseLibrary = "org.postgresql:postgresql:42.5.1"
java.sourceCompatibility = JavaVersion.VERSION_17
//java.sourceCompatibility = JavaVersion.VERSION_1_8

dependencies {
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
  // This dependency is used by the application.
  implementation("com.google.guava:guava:31.1-jre")
  //  implementation("org.jooq:jooq:3.17.6")


  implementation(databaseLibrary)
  //  testImplementation("junit:junit:4.11")

  //  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0")
  implementation("org.springframework.boot:spring-boot-starter")
  implementation("org.springframework.boot:spring-boot-starter-jdbc:3.0.1")
  //  implementation("org.flywaydb:flyway-core:9.11.0")
  implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.0")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0")
  runtimeOnly("org.postgresql:postgresql")
  testImplementation("org.springframework.boot:spring-boot-starter-test")

  testImplementation("io.kotest:kotest-runner-junit5:$KOTEST_VERSION")
  testImplementation("io.kotest:kotest-assertions-core:$KOTEST_VERSION")
  testImplementation("io.kotest:kotest-property:$KOTEST_VERSION")

  //  jooqGenerator(databaseLibrary)
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


//apply ("jooq.gradle.kts")

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

//jooq {
//  version.set("3.17.6")  // default (can be omitted)
//  edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)  // default (can be omitted)
//
//  configurations {
//    create("main") {  // name of the jOOQ configuration
//      generateSchemaSourceOnCompilation.set(true)  // default (can be omitted)
//
//      jooqConfiguration.apply {
//        logging = Logging.WARN
//        jdbc.apply {
//          driver = "org.postgresql.Driver"
//          url = "jdbc:postgresql://localhost:5432/sample"
//          user = "some_user"
//          password = "some_secret"
//          properties.add(Property().apply {
//            key = "ssl"
//            value = "true"
//          })
//        }
//        generator.apply {
//          name = "org.jooq.codegen.DefaultGenerator"
//          database.apply {
//            name = "org.jooq.meta.postgres.PostgresDatabase"
//            inputSchema = "public"
//            forcedTypes.addAll(listOf(
//              ForcedType().apply {
//                name = "varchar"
//                includeExpression = ".*"
//                includeTypes = "JSONB?"
//              },
//              ForcedType().apply {
//                name = "varchar"
//                includeExpression = ".*"
//                includeTypes = "INET"
//              }
//            ))
//          }
//          generate.apply {
//            isDeprecated = false
//            isRecords = true
//            isImmutablePojos = true
//            isFluentSetters = true
//          }
//          target.apply {
//            packageName = "nu.studer.sample"
//            directory = "build/generated-src/jooq/main"  // default (can be omitted)
//          }
//          strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
//        }
//      }
//    }
//  }
//}
