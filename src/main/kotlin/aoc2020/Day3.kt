package aoc2020

import utils.readAllLines

fun countTrees(grid: List<String>, rightCount: Int, downCount: Int): Int {
    var x = 0
    var y = 0
    var treeCount = 0
    while (y < grid.size) {
        if (grid[y][x % grid[y].length] == '#') {
            treeCount++
        }
        x += rightCount
        y += downCount
    }
    return treeCount
}

fun main() {

    val grid = readAllLines(2020, 3)

    println("Part1: ${countTrees(grid, 3, 1)}")

    val toCheck = listOf(listOf(1, 1), listOf(3, 1), listOf(5, 1), listOf(7, 1), listOf(1, 2))
    val treeCounts = toCheck.map {
        countTrees(grid, it[0], it[1]).toLong()
    }

    println("Part2: ${treeCounts.reduce { acc, it -> it * acc }}")
}