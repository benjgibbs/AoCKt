package utils

import kotlin.test.Test
import kotlin.test.assertEquals

internal class UtilsTest {

    @Test
    fun numbersShouldBeExtracted() {
        val longs = longs("12 3456, 789")
        assertEquals(listOf(12L, 3456L, 789L), longs)
    }

    @Test
    fun negativeNumbersAreFine() {
        val longs = longs("12 -3456, 789")
        assertEquals(listOf(12L, -3456L, 789L), longs)
    }

    @Test
    fun doublesAreProblematic() {
        val longs = longs("12.3 3456, 789")
        assertEquals(listOf(12L, 3L, 3456L, 789L), longs)
    }

    @Test
    fun numbersArePulledFromText() {
        val input = """Below is the Advent of Code 2020 overall leaderboard; 
            |these are the 100 users with the highest total score. Getting a star 
            |first is worth 100 points, second is 99, and so on down to 1 point 
            |at 100th place.""".trimMargin()
        assertEquals(listOf(2020L, 100L, 100L, 99L, 1L, 100L), longs(input))
    }

    @Test
    fun doublesCaptureIntsTo() {
        val doubles = reals("12.3 3456, 789")
        assertEquals(listOf(12.3, 3456.0, 789.0), doubles)
    }

}