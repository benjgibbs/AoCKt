package aoc2020

import utils.readAllLines
import kotlin.math.max
import kotlin.math.min

const val ACTIVE = '#'
const val INACTIVE = '.'


fun main() {
    val test1 = listOf(".#.".toList(), "..#".toList(), "###".toList())

    var input = readAllLines(2020, 17).map { it.toList() }
    //input = test1

    println("Part 1: ${part1(input)}");
    println("Part 2: ${part2(input)}");
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

fun neighbours(occupied: Set<Point>, w: Int, x: Int, y: Int, z: Int): Int {
    var count = 0
    for (w2 in (w - 1)..(w + 1)) {
        for (x2 in (x - 1)..(x + 1)) {
            for (y2 in (y - 1)..(y + 1)) {
                for (z2 in (z - 1)..(z + 1)) {
                    //println("checking: $w,$x,$y,$z -> $w2,$x2,$y2,$z2")
                    if (occupied.contains(Point(w2, x2, y2, z2)) && !(w == w2 && z == z2 && y == y2 && x == x2)) {
                        count++
                    }
                }
            }
        }
    }
    return count
}

private fun part2(input: List<List<Char>>): Int {
    var active = mutableSetOf<Point>()
    for (y in input.indices) {
        for (x in input[y].indices) {
            if (input[y][x] == ACTIVE) {
                active.add(Point(0, x, y, 0))
            }
        }
    }
    print(active)
    println("---")
    for (cycle in 0 until 6) {
        val limits = getLimits(active)
        val next = mutableSetOf<Point>()
        for (w in (limits[0].first - 1) until (limits[0].second + 2)) {
            for (x in (limits[1].first - 1) until (limits[1].second + 2)) {
                for (y in (limits[2].first - 1) until (limits[2].second + 2)) {
                    for (z in (limits[3].first - 1) until (limits[3].second + 2)) {
                        val p = Point(w, x, y, z)
                        val ns = neighbours(active, w, x, y, z)
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
        print(next)
        active = next
    }

    return active.size
}

private fun print(active: Set<Point>) {
    val limits = getLimits(active)
    for (z in limits[3].first..limits[3].second) {
        for (w in limits[0].first..limits[0].second) {
            println("z=$z, w=$w")
            for (y in limits[2].first..limits[2].second) {
                println((limits[1].first..limits[1].second)
                    .map{if (active.contains(Point(w, it, y, z))) ACTIVE else INACTIVE }
                    .joinToString("")
                )
            }
        }
    }
    println("~~~")
}

private fun part1(input: List<List<Char>>): Int {
    var grid = mutableListOf(input.toList().map { it.toList() })
    //print(grid)
    grid = resize(grid)
    for (cycle in 0 until 6) {
        var newGrid = mutableListOf<List<List<Char>>>()
        for (z in 0 until grid.size + 1) {
            val newZ = mutableListOf<List<Char>>()
            for (y in grid[0].indices) {
                var newY = mutableListOf<Char>()
                for (x in grid[0][0].indices) {
                    val count = neighbours(grid, x, y, z)
                    if (z == grid.size) {
                        newY.add(if (count == 3) ACTIVE else INACTIVE)
                    } else if (grid[z][y][x] == ACTIVE) {
                        newY.add(if (count in 2..3) ACTIVE else INACTIVE)
                    } else {
                        newY.add(if (count == 3) ACTIVE else INACTIVE)
                    }
                }
                newZ.add(newY)
            }
            newGrid.add(newZ)
        }
        grid = resize(newGrid)
        //print(grid)
    }
    return count(grid)
}

fun neighbours(grid: List<List<List<Char>>>, x: Int, y: Int, z: Int): Int {
    var count = 0
    for (z2 in max(z - 1, 0) until min(z + 2, grid.size)) {
        for (y2 in max(y - 1, 0) until min(y + 2, grid[z2].size)) {
            for (x2 in max(x - 1, 0) until min(x + 2, grid[z2][y2].size)) {
                if (grid[z2][y2][x2] == ACTIVE && !(z == z2 && y == y2 && x == x2)) {
                    if (z == 0 && z2 != 0) {
                        count += 2
                    } else {
                        count++
                    }
                }
            }
        }
    }
    return count
}


fun count(grid: List<List<List<Char>>>): Int {
    var count = 0
    for (z in grid.indices) {
        for (y in grid[z].indices) {
            for (x in grid[z][y].indices) {
                if (grid[z][y][x] == ACTIVE) {
                    if (z == 0) {
                        count++
                    } else {
                        // Assume symmetry
                        count += 2
                    }
                }
            }
        }
    }
    return count
}

fun print(grid: List<List<List<Char>>>) {
    for (z in grid.indices) {
        println("z = $z")
        for (y in grid[z].indices) {
            println(grid[z][y].joinToString(""))
        }
    }
}

fun resize(grid: MutableList<List<List<Char>>>): MutableList<List<List<Char>>> {
    val result = mutableListOf<List<List<Char>>>()
    for (z in grid.indices) {
        val rows = mutableListOf<List<Char>>()
        rows.add((0 until grid[0][0].size + 2).map { '.' })
        for (cells in grid[z]) {
            var newRow = mutableListOf('.')
            newRow.addAll(cells)
            newRow.add('.')
            rows.add(newRow)
        }
        rows.add((0 until grid[0][0].size + 2).map { '.' })
        result.add(rows)
    }
    return result
}