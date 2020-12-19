package aoc2020

import utils.readAllLines
import java.lang.IllegalStateException


fun expand(
    id: Int,
    rules: Map<Int, List<List<Any>>>,
    cache: MutableMap<Int, List<String>>,
    partials: Set<String>
): List<String> {

    if (id in cache) {
        return cache[id]!!
    }

    val result = mutableListOf<String>()
    for (choice in rules[id]!!) {
        val expansion = choice.map { part ->
            when (part) {
                is Int -> expand(part, rules, cache, partials)
                is String -> listOf(part)
                else -> throw IllegalStateException("Unknown $part")
            }
        }
        var choiceResult = expansion[0].toMutableList()
        for (list in expansion.drop(1)) {
            val nextResult = mutableListOf<String>()
            while (choiceResult.isNotEmpty()) {
                val prefix = choiceResult.removeAt(0)
                for (nextPart in list) {
                    val next = prefix + nextPart
                    if (partials.isEmpty() || partials.contains(next)) {
                        nextResult.add(next)
                    }
                }
            }
            choiceResult = nextResult
        }
        result.addAll(choiceResult)
    }
    cache[id] = result
    return result
}

fun main() {
    val cache = mutableMapOf<Int, List<String>>()
    checkExample(cache)
    val lines = readAllLines(2020, 19)

    val rules = mutableMapOf<Int, List<List<Any>>>()
    var parsingRules = true
    val possible = mutableSetOf<String>()
    var count = 0
    val toCheck = mutableListOf<String>()
    for (line in lines) {
        if (line.isEmpty()) {
            parsingRules = false
            possible.addAll(expand(0, rules, cache, setOf()))
        } else if (parsingRules) {
            val parts = line.split(":")
            val ruleId = parts[0].toInt()
            val choices = parts[1].split("|").map { c ->
                c.split(" ").filter { it.isNotEmpty() }.map {
                    it.toIntOrNull() ?: it.subSequence(1, it.length - 1)
                }.toList()
            }.toList()
            rules[ruleId] = choices
        } else {
            toCheck.add(line)
            if (line in possible) {
                count++
            }
        }
    }
    println("Part1: $count")

    //

    val toCheckFor = mutableSetOf<String>()
    for (line in toCheck) {
        for (i in 0 until line.length) {
            for (j in (i + 1) until line.length + 1) {
                toCheckFor.add(line.substring(i, j))
            }
        }
    }
    println(toCheck.filter { it in toCheckFor }.size)

    //println("8: ${expand(8, rules, cache)}")
    //println("11: ${expand(11, rules, cache)}")

    cache.clear()
    rules[8] = listOf(
        listOf(42),
        listOf(42, 42),
        listOf(42, 42, 42),
        listOf(42, 42, 42, 42),
        listOf(42, 42, 42, 42, 42),
        listOf(42, 42, 42, 42, 42, 42),
        listOf(42, 42, 42, 42, 42, 42, 42),
        listOf(42, 42, 42, 42, 42, 42, 42, 42),

    )
    rules[11] = listOf(
        listOf(42, 31),
        listOf(42, 42, 31, 31),
        listOf(42, 42, 42, 31, 31, 31),
        listOf(42, 42, 42, 42, 31, 31, 31, 31),
        listOf(42, 42, 42, 42, 42, 31, 31, 31, 31, 31),
        listOf(42, 42, 42, 42, 42, 42, 31, 31, 31, 31, 31, 31),
    )
    val possibles2 = expand(0, rules, cache, toCheckFor)
    println("Part2: ${toCheck.filter { it in possibles2 }.count()}")
}

private fun checkExample(cache: MutableMap<Int, List<String>>) {
    val example = mapOf(
        Pair(0, listOf(listOf(4, 1, 5))),
        Pair(1, listOf(listOf(2, 3), listOf(3, 2))),
        Pair(2, listOf(listOf(4, 4), listOf(5, 5))),
        Pair(3, listOf(listOf(4, 5), listOf(5, 4))),
        Pair(4, listOf(listOf("a"))),
        Pair(5, listOf(listOf("b"))),
    )
    val exampleResult = expand(0, example, cache, setOf())
    if (listOf("aaaabb", "aaabab", "abbabb", "abbbab", "aabaab", "aabbbb", "abaaab", "ababbb") != exampleResult) {
        throw IllegalStateException("Failed Example")
    }
    cache.clear()
    println(exampleResult)
}