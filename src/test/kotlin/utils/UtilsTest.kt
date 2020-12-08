package utils

import kotlin.test.Test
import kotlin.test.assertEquals

internal class UtilsTest {

    @Test
    fun numbersShouldBeExtracted() {
        val ints = ints("12 3456, 789")
        assertEquals(listOf(12, 3456, 789), ints)
    }

    @Test
    fun negativeNumbersAreFine() {
        val ints = ints("12 -3456, 789")
        assertEquals(listOf(12, -3456, 789), ints)
    }

    @Test
    fun doublesAreProblematic() {
        val ints = ints("12.3 3456, 789")
        assertEquals(listOf(12, 3, 3456, 789), ints)
    }

    @Test
    fun numbersArePulledFromText() {
        val input = """Below is the Advent of Code 2020 overall leaderboard; 
            |these are the 100 users with the highest total score. Getting a star 
            |first is worth 100 points, second is 99, and so on down to 1 point 
            |at 100th place.""".trimMargin()
        assertEquals(listOf(2020, 100, 100, 99, 1, 100), ints(input))
    }

    @Test
    fun doublesCaptureIntsTo() {
        val doubles = reals("12.3 3456, 789")
        assertEquals(listOf(12.3, 3456.0, 789.0), doubles)
    }

}