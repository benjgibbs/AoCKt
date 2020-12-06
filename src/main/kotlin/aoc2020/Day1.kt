package aoc2020

import utils.readAllLines

fun main() {
    val numbers = readAllLines(2020, 1)
        .map { Integer.valueOf(it) }
    for (i in 0 until (numbers.size - 1)) {
        for (j in (i + 1) until numbers.size) {
            if (numbers[i] + numbers[j] == 2020) {
                println("Part 1: ${numbers[i] * numbers[j]}")
            }
        }
    }

    for (i in 0 until (numbers.size - 2)) {
        for (j in (i + 1) until (numbers.size - 1)) {
            for (k in (j + 1) until numbers.size) {
                if (numbers[i] + numbers[j] + numbers[k] == 2020) {
                    println("Part 2: ${numbers[i] * numbers[j] * numbers[k]}")
                }
            }
        }
    }
}