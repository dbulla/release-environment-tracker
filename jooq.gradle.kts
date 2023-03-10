/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/7.6/userguide/building_java_projects.html
 * This project uses @Incubating APIs which are subject to change.
 */
import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Logging
import org.jooq.meta.jaxb.Property
import nu.studer.gradle.jooq.JooqEdition

plugins {
  id("nu.studer.jooq") version "8.1"
}

repositories {
  // Use Maven Central for resolving dependencies.
  mavenCentral()
}

val databaseLibrary = "com.h2database:h2:2.1.214"

buildscript {
  repositories {
    gradlePluginPortal()
  }

  dependencies {
    classpath ("nu.studer:gradle-jooq-plugin:8.1")
  }
}



dependencies {
  jooqGenerator(databaseLibrary)
}

apply ("jooq.gradle.kts")

jooq {
  version.set("3.17.6")  // default (can be omitted)
  edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)  // default (can be omitted)

  configurations {
    create("main") {  // name of the jOOQ configuration
      generateSchemaSourceOnCompilation.set(true)  // default (can be omitted)

      jooqConfiguration.apply {
        logging = Logging.WARN
        jdbc.apply {
          driver = "org.postgresql.Driver"
          url = "jdbc:postgresql://localhost:5432/sample"
          user = "some_user"
          password = "some_secret"
          properties.add(Property().apply {
            key = "ssl"
            value = "true"
          })
        }
        generator.apply {
          name = "org.jooq.codegen.DefaultGenerator"
          database.apply {
            name = "org.jooq.meta.postgres.PostgresDatabase"
            inputSchema = "public"
            forcedTypes.addAll(listOf(
              ForcedType().apply {
                name = "varchar"
                includeExpression = ".*"
                includeTypes = "JSONB?"
              },
              ForcedType().apply {
                name = "varchar"
                includeExpression = ".*"
                includeTypes = "INET"
              }
            ))
          }
          generate.apply {
            isDeprecated = false
            isRecords = true
            isImmutablePojos = true
            isFluentSetters = true
          }
          target.apply {
            packageName = "nu.studer.sample"
            directory = "build/generated-src/jooq/main"  // default (can be omitted)
          }
          strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
        }
      }
    }
  }
}
