package aoc2020

import utils.ints
import utils.readAllLines

data class Tile(val id: Int, val grid: List<String>) {
    fun edges() : List<String> {
        val col1 = grid.map { it[0] }.joinToString("")
        val col2 = grid.map { it[it.length-1] }.joinToString("")

        return listOf(
            grid[0],
            grid[0].reversed(),
            grid[grid.size - 1],
            grid[grid.size - 1].reversed(),
            col1,
            col1.reversed(),
            col2,
            col2.reversed()
        )
    }
}

private fun scanTiles(lines: List<String>) : List<Tile> {
    var id = 0
    var tileGrid = mutableListOf<String>()
    val result = mutableListOf<Tile>()
    for (l in lines){
        if (l.startsWith("Tile")) {
            id = ints(l)[0]
        } else if (l.isBlank()) {
            result.add(Tile(id, tileGrid))
            tileGrid = mutableListOf()
        } else {
            tileGrid.add(l)
        }
    }
    return result
}

fun main() {
    val lines = readAllLines(2020,20)
    val tiles = scanTiles(lines)
    println("numTiles = ${tiles.count()}, vals= ${tiles.map { it.id }}")
    val tileMatches = mutableMapOf<Int, MutableList<Int>>()
    for (t1 in tiles.indices) {
        val t1Edges = tiles[t1].edges().toSet()
        for (t2 in t1+1 until tiles.size) {
            val t2Edges = tiles[t2].edges().toSet()
            if (t1Edges.intersect(t2Edges).isNotEmpty()) {
                if (tiles[t1].id !in tileMatches) {
                    tileMatches[tiles[t1].id] = mutableListOf()
                }
                if (tiles[t2].id !in tileMatches) {
                    tileMatches[tiles[t2].id] = mutableListOf()
                }
                tileMatches[tiles[t1].id]!!.add(tiles[t2].id)
                tileMatches[tiles[t2].id]!!.add(tiles[t1].id)
            }
        }
    }
    val corners = tileMatches.filter { it.value.size < 3 }.map { it.key }
    println(corners)
    println("Part 1: ${corners.fold(1L){acc, i -> acc*i }}")

    val fullPictureIdxs = mutableListOf(*((1..12).map { MutableList(12){0}}.toTypedArray()))

    fullPictureIdxs[0][0] = corners[0]
    for (r in 1 until 12) {
        val prior = fullPictureIdxs[0][r-1]
        println(prior)
    }
    println(tileMatches[corners[0]])


}