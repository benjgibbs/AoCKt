package aoc2020

import utils.ints
import utils.readAllLines
import kotlin.math.sqrt

data class Tile(val id: Int, val grid: List<String>) {
    fun edges(): List<String> {
        val firstCol = grid.map { it.first() }.joinToString("")
        val lastCol = grid.map { it.last() }.joinToString("")

        return listOf(
            grid.first(), grid.first().reversed(), grid.last(), grid.last().reversed(),
            firstCol, firstCol.reversed(), lastCol, lastCol.reversed()
        )
    }

    override fun toString(): String {
        val build = StringBuilder()
        for (c in grid) {
            if (build.isNotEmpty()) build.append("\n")
            build.append(c.map { "$it " }.joinToString(""))
        }
        return build.toString()
    }

    fun flipVertically(): Tile {
        return Tile(id, flipVertically(grid))
    }

    fun flipHorizontally(): Tile {
        return Tile(id, flipHorizontally(grid))
    }

    fun rotate(): Tile {
        return Tile(id, rotate(grid))
    }

    fun leftEdge(): String {
        return grid.map { it.first() }.joinToString("")
    }

    fun rightEdge(): String {
        return grid.map { it.last() }.joinToString("")
    }

    fun topEdge(): String {
        return grid.first()
    }

    fun bottomEdge(): String {
        return grid.last()
    }
}

private fun scanTiles(lines: List<String>): List<Tile> {
    var id = 0
    var tileGrid = mutableListOf<String>()
    val result = mutableListOf<Tile>()
    for (l in lines) {
        when {
            l.startsWith("Tile") -> {
                id = ints(l)[0]
            }
            l.isBlank() -> {
                result.add(Tile(id, tileGrid))
                tileGrid = mutableListOf()
            }
            else -> {
                tileGrid.add(l)
            }
        }
    }
    return result
}

fun main() {
    val lines = readAllLines(2020, 20)
    val tiles = scanTiles(lines)
    val tilesById = tiles.map { Pair(it.id, it) }.toMap()
    val tileMatches = mutableMapOf<Int, MutableList<Int>>()
    val corners = findCorners(tiles, tileMatches)
    println("Part 1: ${corners.fold(1L) { acc, i -> acc * i }}")

    val size = sqrt(tiles.size.toDouble()).toInt()
    val fullPictureIdxs = locateTiles(size, corners, tileMatches)
    val rotatedTiles = assembleTiles(size, tilesById, fullPictureIdxs)
    val image = removeBorders(size, rotatedTiles)
    val perms = allPerms(image).map { perm -> Pair(countNessies(perm), perm) }
    val mostNessies = perms.maxByOrNull { it.first }!!.second

    println("Part2: ${countRoughSeas(mostNessies)}")
}

private fun locateTiles(
    size: Int,
    corners: List<Int>,
    tileMatches: MutableMap<Int, MutableList<Int>>
): MutableList<MutableList<Int>> {
    val fullPictureIdxs = mutableListOf(*((1..size).map { MutableList(size) { 0 } }.toTypedArray()))
    fullPictureIdxs[0][0] = corners[0]

    val added = mutableSetOf(corners[0])
    //first row
    for (r in 1 until size) {
        val prior = fullPictureIdxs[0][r - 1]
        val possibles = tileMatches[prior]!!
            .filter { it !in added }
            .map { Pair(it, tileMatches[it]!!.size) }
            .sortedBy { it.second }

        fullPictureIdxs[0][r] = possibles[0].first
        added.add(possibles[0].first)
    }
    for (c in 1 until size) {
        for (r in 0 until size) {
            val prior = fullPictureIdxs[c - 1][r]
            val possibles = tileMatches[prior]!!
                .filter { it !in added }
                .map { Pair(it, tileMatches[it]!!.size) }
                .sortedBy { it.second }
            fullPictureIdxs[c][r] = possibles[0].first
            added.add(possibles[0].first)
        }
    }
    return fullPictureIdxs
}

private fun removeBorders(
    size: Int,
    rotatedTiles: List<MutableList<Tile>>
): MutableList<String> {
    val image = mutableListOf<String>()
    for (r in 0 until size * 10) {
        if (r % 10 == 0 || r % 10 == 9) {
            continue
        }
        val buffer = StringBuilder()
        for (c in 0 until size) {
            buffer.append(rotatedTiles[r / 10][c].grid[r % 10].substring(1, 9))
        }
        image.add(buffer.toString())
    }
    return image
}

private fun assembleTiles(
    size: Int,
    tilesById: Map<Int, Tile>,
    fullPictureIdxs: MutableList<MutableList<Int>>
): List<MutableList<Tile>> {
    val rotatedTiles: List<MutableList<Tile>> = (1..size).map { MutableList(size) { Tile(-1, listOf()) } }

    for (r in rotatedTiles.indices) {
        for (c in rotatedTiles.indices) {
            if (c == 0) {
                if (r == 0) {
                    // Constrain to left tile
                    val t0 = tilesById[fullPictureIdxs[r][c]]!!
                    val t1 = tilesById[fullPictureIdxs[r][c + 1]]!!

                    for (tt0 in allPerms(t0)) {
                        for (tt1 in allPerms(t1)) {
                            if (tt0.rightEdge() == tt1.leftEdge()) {
                                rotatedTiles[r][c] = tt0
                            }
                        }
                    }
                } else {
                    // Constrain to tile above
                    val t0 = rotatedTiles[r - 1][c]
                    val t1 = tilesById[fullPictureIdxs[r][c]]!!

                    for (tt1 in allPerms(t1)) {
                        if (t0.bottomEdge() == tt1.topEdge()) {
                            rotatedTiles[r][c] = tt1
                        }
                    }
                }

            } else {
                // Constrain to left tile
                val t0 = rotatedTiles[r][c - 1]
                val t1 = tilesById[fullPictureIdxs[r][c]]!!

                for (tt1 in allPerms(t1)) {
                    if (t0.rightEdge() == tt1.leftEdge()) {
                        rotatedTiles[r][c] = tt1
                    }
                }
            }
        }
    }
    return rotatedTiles
}

private fun findCorners(
    tiles: List<Tile>,
    tileMatches: MutableMap<Int, MutableList<Int>>
): List<Int> {
    for (t1 in tiles.indices) {
        val t1Edges = tiles[t1].edges().toSet()
        for (t2 in t1 + 1 until tiles.size) {
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
    return corners
}

fun flipVertically(grid: List<String>): List<String> {
    return grid.map { line ->
        line.reversed()
    }
}

fun flipHorizontally(grid: List<String>): List<String> {
    val newGrid = MutableList(grid.size) { "" }
    for (g in grid.indices) {
        newGrid[grid.size - 1 - g] = grid[g]
    }
    return newGrid
}

fun rotate(grid: List<String>): List<String> {
    val newGrid = grid.indices.map { MutableList(grid.size) { '.' } }.toList()
    for (r in grid.indices) {
        for (c in grid.indices) {
            newGrid[c][grid.size - 1 - r] = grid[r][c]
        }
    }
    return newGrid.map { it.joinToString("") }
}

fun countRoughSeas(image: List<String>): Int {
    val hashes = image.fold(0) { acc, line -> acc + line.count { it == '#' } }
    val nessies = countNessies(image)
    return hashes - nessies
}

fun countNessies(image: List<String>): Int {
    /*0 012345678901234567890
      0                   #
      1 #    ##    ##    ###
      2  #  #  #  #  #  #
        012345678901234567890
     */

    val offsets = mutableListOf(
        Pair(0, 18),
        Pair(1, 0), Pair(1, 5), Pair(1, 6), Pair(1, 11), Pair(1, 12), Pair(1, 17), Pair(1, 18), Pair(1, 19),
        Pair(2, 1), Pair(2, 4), Pair(2, 7), Pair(2, 10), Pair(2, 13), Pair(2, 16),
    )

    var count = 0
    for (r in 0 until image.size - 3) {
        for (c in 0 until image[r].length - 19) {
            val points = offsets.map { Pair(it.first + r, it.second + c) }
            if (points.map { image[it.first][it.second] }.count { it == '#' } == offsets.size) {
                count++
            }
        }
    }
    return count * offsets.size
}

fun allPerms(t: Tile): List<Tile> {
    val result = mutableListOf<Tile>()
    result.add(t)
    result.add(t.rotate())
    result.add(t.rotate().rotate())
    result.add(t.rotate().rotate().rotate())
    result.add(t.flipVertically())
    result.add(t.flipVertically().rotate())
    result.add(t.flipHorizontally())
    result.add(t.flipHorizontally().rotate())
    return result
}

fun allPerms(grid: List<String>): List<List<String>> {
    val result = mutableListOf<List<String>>()
    result.add(grid)
    result.add(rotate(grid))
    result.add(rotate(rotate(grid)))
    result.add(rotate(rotate(rotate(grid))))
    result.add(flipVertically(grid))
    result.add(rotate(flipVertically(grid)))
    result.add(flipHorizontally(grid))
    result.add(rotate(flipHorizontally(grid)))
    return result
}



