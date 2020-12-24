package aoc2020

import utils.readAllLines

data class ARC(val a: Int, val r: Int, val c: Int) {
    fun nw(): ARC {
        return ARC(1 - a, r - (1 - a), c - (1 - a))
    }

    fun ne(): ARC {
        return ARC(1 - a, r - (1 - a), c + a)
    }

    fun sw(): ARC {
        return ARC(1 - a, r + a, c - (1 - a))
    }

    fun se(): ARC {
        return ARC(1 - a, r + a, c + a)
    }

    fun w(): ARC {
        return ARC(a, r, c - 1)
    }

    fun e(): ARC {
        return ARC(a, r, c + 1)
    }

    fun adjacent(): Set<ARC> {
        return setOf(nw(), ne(), sw(), se(), w(), e())
    }
}

fun main() {
    val lines = readAllLines(2020, 24)
    val blackTiles = mutableSetOf<ARC>()

    for (line in lines) {
        var i = 0
        var pos = ARC(0, 0, 0)
        while (i < line.length) {
            pos = when (line[i]) {
                'n' -> {
                    i++
                    when (line[i]) {
                        'e' -> pos.ne()
                        'w' -> pos.nw()
                        else -> throw IllegalArgumentException()
                    }
                }
                's' -> {
                    i++
                    when (line[i]) {
                        'e' -> pos.se()
                        'w' -> pos.sw()
                        else -> throw IllegalArgumentException()
                    }
                }
                'e' -> pos.e()
                'w' -> pos.w()
                else -> throw IllegalArgumentException()
            }
            i++
        }

        if (pos in blackTiles) {
            blackTiles.remove(pos)
        } else {
            blackTiles.add(pos)
        }
    }
    println("Part1: ${blackTiles.size}")

    var currentlyBlack = blackTiles

    for (i in 1..100) {

        val counts = mutableMapOf<ARC, Int>()
        for (tile in currentlyBlack) {
            for (adj in tile.adjacent()) {
                val c = counts.getOrDefault(adj, 0)
                counts[adj] = c + 1
            }
        }

        val nextBlack = mutableSetOf<ARC>()
        for (entry in counts) {
            val tile = entry.key
            val neighbours = entry.value

            if (tile in currentlyBlack) {
                if (neighbours in 1..2) {
                    nextBlack.add(tile)
                }
            } else {
                if (neighbours == 2) {
                    nextBlack.add(tile)
                }
            }
        }
        currentlyBlack = nextBlack
    }

    println("Part2: ${currentlyBlack.size}")
}

