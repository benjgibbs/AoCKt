package aoc2019

import utils.readAllLines

fun main() {
    val lines = readAllLines(2019, 1)
    var fuel = 0
    var fuel2 = 0
    lines.map { Integer.valueOf(it) }.forEach {
        val f1 = it / 3 - 2
        fuel += f1

        var f2 = f1
        while (f2 > 0) {
            fuel2 += f2
            f2 = f2 / 3 - 2
        }
    }
    println("Fuel: $fuel")
    println("Fuel2: $fuel2")
}