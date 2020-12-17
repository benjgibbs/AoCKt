package aoc2020

import utils.ints
import utils.readAllLines
import kotlin.math.abs

fun main() {
    val input = readAllLines(2020, 16)

    val valid = mutableSetOf<Int>()
    val myTicket = mutableListOf<Int>()
    var part = 0
    var errorRate = 0
    val fieldValid = ArrayList<Set<Int>>(20)
    val validTickets = mutableListOf<List<Int>>()

    for (line in input) {
        if (line.isEmpty()) {
            part++
        } else {
            val ints = ints(line)
            if (part == 0) {
                val validSet = mutableSetOf<Int>()
                validSet.addAll(ints[0]..abs(ints[1]))
                validSet.addAll(ints[2]..abs(ints[3]))
                valid.addAll(validSet)
                fieldValid.add(validSet)

            } else if (part == 1) {
                if (ints.isNotEmpty()) {
                    myTicket.addAll(ints)
                }
            } else {
                if (ints.isNotEmpty()) {
                    val ticketInts = HashSet(ints)
                    ticketInts.removeAll(valid)
                    if (ticketInts.size > 0) {
                        errorRate += ticketInts.sum()
                    } else {
                        validTickets.add(ints)
                    }

                }
            }
        }
    }
    println("Part1: $errorRate")

    val possibles = mutableListOf<MutableSet<Int>>()

    for (c in 0 until 20) {
        val check = fieldValid[c]

        val possible = (0..19).toMutableSet()
        for (t in validTickets) {
            for ((i, f) in t.withIndex()) {
                if (!check.contains(f)) {
                    possible.remove(i)
                }
            }
        }
        possibles.add(possible)
    }
    val sureThings = mutableMapOf<Int, Int>()
    while (true) {
        val removeVal = mutableSetOf<Int>()
        for ((i, p) in possibles.withIndex()) {
            if (p.size == 1) {
                sureThings[i] = p.first()
                removeVal.add(p.first())
            }
        }
        if (removeVal.isEmpty()) {
            break
        }
        for (i in removeVal) {
            for (l in possibles) {
                l.remove(i)
            }
        }
    }
    val part2 = (0..5)
        .map { myTicket[sureThings[it]!!] }
        .fold(1L) { acc, x -> acc * x }

    println("Part2: $part2")
}