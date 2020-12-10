package aoc2020

import utils.readAllLines
import java.lang.IllegalStateException

fun count(adaptors: List<Int>): Long {

    var cache = HashMap<Int, Long>()
    val list = adaptors.sortedDescending().plus(0)

    cache[list.first()] = 1

    for (i in 1 until list.size) {
        val jolts = list[i]
        var orders = 0L
        for (j in jolts+1..jolts+3){
            orders += cache[j] ?: 0
        }
        cache[jolts] = orders
    }

    return cache[0] ?: 1

}

fun main() {
    val jolts = readAllLines(2020,10).map{it.toInt()}
    //val jolts = test1.split("\n").map { it.toInt() }
    //val jolts = test2.split("\n").map { it.toInt() }
    val sorted = jolts.sorted()

    println("part1: ${part1(sorted)}")
    println("part2: ${count(sorted)}")

}

private fun part1(sorted: List<Int>): Int {
    var ones = 0
    var threes = 1
    var current = 0
    for (i in sorted) {
        current += when (current) {
            i - 1 -> {
                ones++
                1
            }
            i - 3 -> {
                threes++
                3
            }
            else -> {
                throw IllegalStateException()
            }
        }
    }

    return ones * threes
}

val test1 = "16\n" +
        "10\n" +
        "15\n" +
        "5\n" +
        "1\n" +
        "11\n" +
        "7\n" +
        "19\n" +
        "6\n" +
        "12\n" +
        "4"

val test2 = "28\n" +
        "33\n" +
        "18\n" +
        "42\n" +
        "31\n" +
        "14\n" +
        "46\n" +
        "20\n" +
        "48\n" +
        "47\n" +
        "24\n" +
        "23\n" +
        "49\n" +
        "45\n" +
        "19\n" +
        "38\n" +
        "39\n" +
        "11\n" +
        "1\n" +
        "32\n" +
        "25\n" +
        "35\n" +
        "8\n" +
        "17\n" +
        "7\n" +
        "9\n" +
        "4\n" +
        "2\n" +
        "34\n" +
        "10\n" +
        "3"