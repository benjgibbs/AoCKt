package aoc2020

import utils.readAllLines
import kotlin.math.max
import kotlin.math.min

const val ACTIVE = '#'

fun main() {
    val input = readAllLines(2020, 17).map { it.toList() }
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

data class Point(val w: Int, val x: Int, val y: Int, val z: Int)

fun getLimits(active: Set<Point>): List<Pair<Int, Int>> {
    val initLimit = Pair(Int.MAX_VALUE, Int.MIN_VALUE)
    val wLimit = active.fold(initLimit) { acc, p -> Pair(min(acc.first, p.w), max(acc.second, p.w)) }
    val xLimit = active.fold(initLimit) { acc, p -> Pair(min(acc.first, p.x), max(acc.second, p.x)) }
    val yLimit = active.fold(initLimit) { acc, p -> Pair(min(acc.first, p.y), max(acc.second, p.y)) }
    val zLimit = active.fold(initLimit) { acc, p -> Pair(min(acc.first, p.z), max(acc.second, p.z)) }
    return listOf(wLimit, xLimit, yLimit, zLimit)
}

fun neighbours(occupied: Set<Point>, w: Int, x: Int, y: Int, z: Int, useW: Boolean): Int {
    var count = 0
    for (w2 in if (useW) (w - 1)..(w + 1) else listOf(w)) {
        for (x2 in (x - 1)..(x + 1)) {
            for (y2 in (y - 1)..(y + 1)) {
                for (z2 in (z - 1)..(z + 1)) {
                    if (occupied.contains(Point(w2, x2, y2, z2)) &&
                        !(w == w2 && z == z2 && y == y2 && x == x2)) {
                        count++
                    }
                }
            }
        }
    }
    return count
}

private fun part2(input: List<List<Char>>): Int {
    var active = initialActives(input)
    for (cycle in 0 until 6) {
        val limits = getLimits(active)
        val next = mutableSetOf<Point>()
        for (w in (limits[0].first - 1) until (limits[0].second + 2)) {
            for (x in (limits[1].first - 1) until (limits[1].second + 2)) {
                for (y in (limits[2].first - 1) until (limits[2].second + 2)) {
                    for (z in (limits[3].first - 1) until (limits[3].second + 2)) {
                        val p = Point(w, x, y, z)
                        val ns = neighbours(active, w, x, y, z, true)
                        if (active.contains(p)) {
                            if (ns in 2..3) {
                                next.add(p)
                            }
                        } else if (ns == 3) {
                            next.add(p)
                        }
                    }
                }
            }
        }
        active = next
    }

    return active.size
}

private fun part1(input: List<List<Char>>): Int {
    var active = initialActives(input)
    for (cycle in 0 until 6) {
        val limits = getLimits(active)
        val next = mutableSetOf<Point>()
            for (x in (limits[1].first - 1) until (limits[1].second + 2)) {
                for (y in (limits[2].first - 1) until (limits[2].second + 2)) {
                    for (z in (limits[3].first - 1) until (limits[3].second + 2)) {
                        val p = Point(0, x, y, z)
                        val ns = neighbours(active, 0, x, y, z, false)
                        if (active.contains(p)) {
                            if (ns in 2..3) {
                                next.add(p)
                            }
                        } else if (ns == 3) {
                            next.add(p)
                        }
                    }
                }
        }
        active = next
    }
    return active.size
}

private fun initialActives(input: List<List<Char>>): MutableSet<Point> {
    val active = mutableSetOf<Point>()
    for (y in input.indices) {
        for (x in input[y].indices) {
            if (input[y][x] == ACTIVE) {
                active.add(Point(0, x, y, 0))
            }
        }
    }
    return active
}