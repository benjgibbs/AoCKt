package aoc2020

import utils.readAllLines
import java.lang.IllegalStateException
import java.lang.Long.min
import java.util.*
import kotlin.math.max

fun main(){
    val input = readAllLines(2020, 9).map { it.toLong() }
    val q = LinkedList<Long>()
    val part1: Long = part1(input, q)
    println("Part1: $part1")
    println("Part2: ${part2(input, part1)}")
}

private fun part2(input: List<Long>, part1: Long) : Long {
    for (a in 0 until (input.size - 1)) {
        for (b in a + 1 until input.size) {
            var sum = 0L
            var min = Long.MAX_VALUE
            var max = Long.MIN_VALUE
            for (c in a..b) {
                sum += input[c]
                min = min(min, input[c])
                max = max(max, input[c])
            }
            if (sum == part1) {
                return min+max
            }
        }
    }
    throw IllegalStateException("Result not found")
}

private fun part1(input: List<Long>, q: LinkedList<Long>): Long {
    var part1: Long = 0
    for (i in input) {
        if (q.size == 25) {
            var found = false
            for (a in 0 until (q.size - 1)) {
                val toCheck = i - q[a]
                for (b in a + 1 until q.size) {
                    if (q[b] == toCheck) {
                        found = true
                        break
                    }
                }
                if (found) {
                    break
                }
            }
            if (!found) {
                part1 = i
            }
            q.removeFirst()
        }
        q.addLast(i)
    }
    return part1
}