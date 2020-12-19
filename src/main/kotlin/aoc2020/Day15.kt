package aoc2020

import utils.ints

fun checkExample(starting: List<Int>, turns: Int): Int {
    val start = System.currentTimeMillis()
    val cache = mutableMapOf<Int, Int>()
    var spoken = 0
    for (t in 1..turns) {
        val last = spoken
        spoken = if (t <= starting.size) {
            starting[t - 1]
        } else {
            if (cache.contains(last)) {
                (t - 1) - cache[last]!!
            } else {
                0
            }
        }
        cache[last] = t - 1
    }
    println("Took: ${System.currentTimeMillis() - start}")
    return spoken
}

fun main() {
    val input = ints("19,0,5,1,10,13")
    println("Check: ${checkExample(listOf(0, 3, 6), 10)}")
    println("Part1: ${checkExample(input, 2020)}")
    println("Part2: ${checkExample(input, 30000000)}")
}