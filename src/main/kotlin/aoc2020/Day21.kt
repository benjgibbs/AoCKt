package aoc2020

import utils.readAllLines

fun main() {
    val lines = readAllLines(2020, 21)
    val wordsRe = """(\w+)""".toRegex()

    val input = lines.map { l ->
        val words = wordsRe.findAll(l).map { it.value }.toList()
        val containsIds = words.indexOf("contains")
        Pair(words.subList(0, containsIds), words.subList(containsIds + 1, words.size))
    }
    val allergens = input.flatMap { it.second }.toSet()

    val possTranslations = mutableListOf<Pair<String, MutableSet<String>>>()

    for (a in allergens) {

        val containsA = input.filter { it.second.contains(a) }
        var set = containsA.get(0).first.toSet()
        for (i in containsA.drop(0)) {
            set = set.intersect(i.first)
        }
        println("a=$a, set=$set")
        possTranslations.add(Pair(a, set.toMutableSet()))
    }

    while (possTranslations.maxOf { it.second.size } > 1) {
        possTranslations.sortedBy { it.second.size }
        val removeFromOthers = possTranslations.filter { it.second.size == 1 }.map { it.second.first() }.toSet()
        for (t in possTranslations.filter { it.second.size > 1 }) {
            t.second.removeAll(removeFromOthers)
        }
    }
    val translations = possTranslations.map { Pair(it.first, it.second.first()) }.toMap()
    val notAllergen = input.flatMap { it.first }.filter { it !in translations.values }
    println("Part1: ${notAllergen.size}")

    val pairs = possTranslations.map { Pair(it.first, it.second.first()) }

    println("Part2: ${pairs.sortedBy { it.first }.map { it.second }.joinToString(",")}")
}