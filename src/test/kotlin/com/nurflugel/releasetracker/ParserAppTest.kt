/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package com.nurflugel.releasetracker

import java.time.LocalDateTime
import java.time.temporal.TemporalAccessor
import kotlin.test.Test
import kotlin.test.assertEquals

class ParserAppTest {

  private val dateTime: LocalDateTime = LocalDateTime.of(2023, 12, 25, 10, 15)
  private val stringDateTime = "2023-12-25 10&15"

  @Test
  fun testToString() {
    val result: String = DataRecord.formatter.format(dateTime)
    assertEquals(result, stringDateTime, result)
  }

  @Test
  fun testFromString() {
    val result: LocalDateTime = LocalDateTime.parse(stringDateTime.trim(), DataRecord.formatter)
    assertEquals(dateTime, result)
  }
}
