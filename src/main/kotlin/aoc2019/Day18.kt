package aoc2019

import utils.readAllLines
import java.lang.IllegalStateException

fun find(grid: List<String>, char: Char) : Pair<Int, Int> {
    for (l in grid.withIndex()) {
        for (c in l.value.withIndex()) {
            if (c.value == char) {
                return Pair(c.index, l.index)
            }
        }
    }
    throw IllegalStateException("Failed to find: $char")
}

fun allMoves(pos: Pair<Int, Int>) : List<Pair<Int, Int>> {
    return listOf(Pair(-1, 0), Pair(1, 0), Pair(0, 1), Pair(0, -1))
        .map{ m -> Pair(m.first + pos.first, m.second + pos.second)}
}

fun validMoves(grid: List<String>, pos: Pair<Int, Int>) : List<Pair<Int, Int>> {
    val allMoves = allMoves(pos)
    return allMoves.filter {
        it.second >= 0 && it.second < grid.size &&
        it.first >= 0 && it.first < grid[it.second].length
    }.filter {
        grid[it.second][it.first] != '#'
    }
}

fun main() {
    val grid = readAllLines(2019, 18, "Example")
    val pos = find(grid, '@')
    println(validMoves(grid, pos))
    val foundKeys = mutableSetOf<Char>()
    val foundDoors = mutableSetOf<Char>()
    var currentPos = pos

}

