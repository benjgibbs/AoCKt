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

    @Test
    fun euclidFindsGCD() {
        assertEquals(3, euclid(3,9)[0])
        assertEquals(3, euclid(3,12)[0])
        assertEquals(3, euclid(6,9)[0])
        assertEquals(14, euclid(345688,245994)[0])
    }

    @Test
    fun checkCRT() {
        assertEquals(53, crt(listOf(2L, 5L, -3L), listOf(3L, 4L, 7L)))
        assertEquals(158, crt(listOf(3L, 2L, 4L), listOf(5L, 6L, 7L)))
    }

    @Test
    fun checkTokenRegEx() {
        val input = "12*6*(8 + 8 * 4 + 3 + 6) + (4 + 3 * 7 + 7 + 4 + 2) + 7 + (3 + (2 * 8 + 7 + 7) * 7 + 5 * (3 * 2 + 9 + 2 + 8 + 7) * (4 + 2 + 8))"

        val tokenRegex = """-?[0-9.]+|\+|\*|\(|\)""".toRegex()
        val tokens = tokenRegex.findAll(input).map { it.groupValues[0] }.toList()
        println("Tokens: $tokens")


    }

}