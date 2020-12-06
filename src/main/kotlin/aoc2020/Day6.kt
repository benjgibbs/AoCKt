package aoc2020

import utils.readAllLines
import java.lang.StringBuilder

fun main() {
    val lines = readAllLines(2020,6)

    val letters = mutableSetOf<Char>()
    var count = 0

    for (line in lines) {
        if (line.isNotEmpty()){
            letters.addAll(line.asIterable())
        } else {
            count += letters.size
            letters.clear()
        }
    }
    count += letters.size
    println("Part 1: ${count}")


    val sets = mutableListOf<Set<Char>>()
    count = 0
    for (line in lines){
        if (line.isNotEmpty()){
            sets.add(HashSet(line.toList()))
        } else {
            val c = sets.reduce{ acc, s -> acc.intersect(s)}.size
            count += c
            sets.clear()
        }
    }
    val c = sets.reduce{ acc, s -> acc.intersect(s)}.size
    count += c
    println("Part 2: $count")
}
