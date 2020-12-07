package aoc2020

import utils.readAllLines
import java.lang.IllegalStateException

data class Contents(val count: Int, val colour: String)

fun main() {
    val lines = readAllLines(2020, 7)
    val map = mutableMapOf<String, List<Contents>>()
    val mapBack = mutableMapOf<String, MutableSet<String>>()
    val keyRegex = """([a-z ]+) bags contain""".toRegex()
    val valRegex = """(\d+) ([a-z ]+) bags?[,.]""".toRegex()

    for (line in lines) {

        val keyMatch = keyRegex.find(line)
        val valMatches = valRegex.findAll(line).toList()

        if (keyMatch != null) {
            val key = keyMatch.groupValues[1]
            val contents = valMatches.map { matchResult ->
                val count = matchResult.groupValues[1].toInt()
                val colour = matchResult.groupValues[2]
                Contents(count, colour)
            }
            map[key] = contents

            valMatches.forEach { mr ->
                mapBack.getOrPut(mr.groupValues[2]) { mutableSetOf() }.add(key)
            }

        } else {
            println("Failed to match: $line")
        }
    }

    println("Part 1: ${part1(mapBack)}")
    println("Part 2: ${count("shiny gold", map) - 1}")
}

private fun count(what: String, map: Map<String, List<Contents>>): Long {
    var result = 1L
    for (entry in map[what] ?: emptyList()) {
        result += (entry.count * count(entry.colour, map))
    }
    return result
}

private fun part1(mapBack: MutableMap<String, MutableSet<String>>): Int {
    var count = 0
    val start = mapBack["shiny gold"] ?: throw IllegalStateException("Missing key")
    val toCount = start.toMutableList()
    val seen = mutableSetOf<String>()
    while (toCount.isNotEmpty()) {

        val thisOne = toCount.removeFirst()
        if (!seen.contains(thisOne)) {
            seen.add(thisOne)
            count++
            val toAdd = mapBack[thisOne] ?: emptyList()
            toCount.addAll(toAdd)
        }
    }
    return count
}
