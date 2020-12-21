package aoc2020

import utils.ints
import utils.readAllLines
import java.lang.StringBuilder

data class Tile(val id: Int, val grid: List<String>) {
    fun edges(): List<String> {
        val col1 = grid.map { it[0] }.joinToString("")
        val col2 = grid.map { it[it.length - 1] }.joinToString("")

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

    override fun toString(): String {
        val build = StringBuilder()
        for (c in grid) {
            if (build.isNotEmpty()) build.append("\n")
            build.append(c.map { "$it " }.joinToString(""))
        }
        return build.toString()
    }

    fun flipVertically(): Tile {
        return Tile(id, grid.map { line ->
            line.reversed()
        })
    }

    fun flipHorizontally(): Tile {
        val newGrid = MutableList<String>(grid.size) { "" }
        for (g in grid.indices) {
            newGrid[grid.size - 1 - g] = grid[g]
        }
        return Tile(id, newGrid)
    }

    fun rotate(): Tile {
        val newGrid = grid.indices.map { MutableList(grid.size) { '.' } }.toList()
        for (r in grid.indices) {
            for (c in grid.indices) {
                newGrid[c][grid.size - 1 - r] = grid[r][c]
            }
        }
        return Tile(id, newGrid.map { it.joinToString("") })
    }

    fun topEdge(): String {
        return grid.first()
    }

    fun bottomEdge(): String {
        return grid.last()
    }

    fun leftEdge(): String {
        return grid.map { it.first() }.joinToString("")
    }

    fun rightEdge(): String {
        return grid.map { it.last() }.joinToString("")
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
    //println("numTiles = ${tiles.count()}, vals= ${tiles.map { it.id }}")
    val tileMatches = mutableMapOf<Int, MutableList<Int>>()
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
    println(corners)
    println("Part 1: ${corners.fold(1L) { acc, i -> acc * i }}")

    val fullPictureIdxs = mutableListOf(*((1..12).map { MutableList(12) { 0 } }.toTypedArray()))

    fullPictureIdxs[0][0] = corners[0]
    val added = mutableSetOf(corners[0])
    //first row
    for (r in 1 until 12) {
        val prior = fullPictureIdxs[0][r - 1]
        val possibles = tileMatches[prior]!!
            .filter { it !in added }
            .map { Pair(it, tileMatches[it]!!.size) }
            .sortedBy { it.second }

        //println(possibles)
        fullPictureIdxs[0][r] = possibles[0].first
        added.add(possibles[0].first)
    }
    for (c in 1 until 12) {
        for (r in 0 until 12) {
            val prior = fullPictureIdxs[c - 1][r]
            val possibles = tileMatches[prior]!!
                .filter { it !in added }
                .map { Pair(it, tileMatches[it]!!.size) }
                .sortedBy { it.second }
            fullPictureIdxs[c][r] = possibles[0].first
            added.add(possibles[0].first)
        }
    }

    println("tiles.size=${tiles.size}, added.size=${added.size}")
    println("Corn")
    for (c in fullPictureIdxs) {
        println(c.joinToString(","))
    }


    val rotatedTiles: List<MutableList<Tile>> = (1..12).map { MutableList(12) { Tile(-1, listOf()) } }

    for (r in rotatedTiles.indices) {
        for (c in rotatedTiles.indices) {
            if (c == 0) {
                val t0 = tiles.find { it.id == fullPictureIdxs[r][c] }!!
                val t1 = tiles.find { it.id == fullPictureIdxs[r][c + 1] }!!

                for (tt0 in allPerms(t0)) {
                    for (tt1 in allPerms(t1)) {
                        if (tt0.rightEdge() == tt1.leftEdge()) {
                            rotatedTiles[r][c] = tt0
                        }
                    }
                }

            } else {
                val t0 = rotatedTiles[r][c - 1]
                val t1 = tiles.find { it.id == fullPictureIdxs[r][c] }!!

                for (tt1 in allPerms(t1)) {
                    if (t0.rightEdge() == tt1.leftEdge()) {
                        rotatedTiles[r][c] = tt1
                    }
                }
            }
        }
    }
    println("---")
    println(rotatedTiles.map { it.map { it.id }.joinToString(",") }.joinToString("\n"))
    println("---")
    println()


    val image = mutableListOf<String>()
    for (r in 0 until 120) {
        if (r % 10 == 0 || r % 10 == 9) {
            continue
        }
        val buffer = StringBuilder()
        for (c in 0 until 12) {
            buffer.append(rotatedTiles[r / 10][c].grid[r % 10].substring(1, 9))
        }
        image.add(buffer.toString())
    }

    println(image[0].length)
    println(image.size)


    val hashes = image.fold(0) { acc, line -> acc + line.count { it == '#' } }
    val nessies = countNessies(image)

    //1749 too high
    println("Part2: ${hashes - nessies * 15}")


    val hashes2 = demoGrid.fold(0) { acc, line -> acc + line.count { it == '#' } }
    val nessies2 = countNessies(demoGrid)

    println(hashes2 - nessies2 * 15)

}

fun countNessies(image: List<String>): Int {
    /*0 012345678901234567890
      0                   #
      1 #    ##    ##    ###
      2  #  #  #  #  #  #
        012345678901234567890
     */
    var count = 0
    for (r in 0 until image.size - 3) {
        for (c in 0 until image[r].length - 19) {
            if (
                image[r][c + 18] == '#'
                && image[r + 1][c] == '#'
                && image[r + 1][c + 5] == '#'
                && image[r + 1][c + 6] == '#'
                && image[r + 1][c + 11] == '#'
                && image[r + 1][c + 12] == '#'
                && image[r + 1][c + 17] == '#'
                && image[r + 1][c + 18] == '#'
                && image[r + 1][c + 19] == '#'
                && image[r + 2][c + 1] == '#'
                && image[r + 2][c + 4] == '#'
                && image[r + 2][c + 7] == '#'
                && image[r + 2][c + 10] == '#'
                && image[r + 2][c + 13] == '#'
                && image[r + 2][c + 16] == '#'
            ) {
                count++
            }
        }
    }
    return count
}

fun allPerms(t: Tile): List<Tile> {
    val result = mutableListOf<Tile>()
    result.add(t)
    result.add(t.rotate())
    result.add(t.rotate().rotate())
    result.add(t.rotate().rotate().rotate())
    result.add(t.flipVertically())
    result.add(t.flipVertically().rotate())
    result.add(t.flipVertically().rotate().rotate())
    result.add(t.flipHorizontally())
    result.add(t.flipHorizontally().rotate())
    result.add(t.flipHorizontally().rotate().rotate())
    return result
}

val demoGrid = listOf(
    ".####...#####..#...###..",
    "#####..#..#.#.####..#.#.",
    ".#.#...#.###...#.##.##..",
    "#.#.##.###.#.##.##.#####",
    "..##.###.####..#.####.##",
    "...#.#..##.##...#..#..##",
    "#.##.#..#.#..#..##.#.#..",
    ".###.##.....#...###.#...",
    "#.####.#.#....##.#..#.#.",
    "##...#..#....#..#...####",
    "..#.##...###..#.#####..#",
    "....#.##.#.#####....#...",
    "..##.##.###.....#.##..#.",
    "#...#...###..####....##.",
    ".#.##...#.##.#.#.###...#",
    "#.###.#..####...##..#...",
    "#.###...#.##...#.######.",
    ".###.###.#######..#####.",
    "..##.#..#..#.#######.###",
    "#.#..##.########..#..##.",
    "#.#####..#.#...##..#....",
    "#....##..#.#########..##",
    "#...#.....#..##...###.##",
    "#..###....##.#...##.##.#"
)



