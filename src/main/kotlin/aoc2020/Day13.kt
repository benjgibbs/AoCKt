package aoc2020

import utils.crt
import utils.longs
import utils.readAllLines
import java.lang.Math.floorMod

fun main() {
    val lines = readAllLines(2020, 13)
    val ints = longs(lines.joinToString(","))
    val earliest = ints[0]
    var minWait = Int.MAX_VALUE
    var id = Int.MAX_VALUE
    for (i in ints.drop(1)) {
        var depart = i
        while (depart < earliest) {
            depart += i
        }
        val wait = (depart - earliest)
        if (wait < minWait) {
            minWait = wait.toInt()
            id = i.toInt()
        }
    }
    println("Part1: ${id * minWait}")

    data class Bus(val depart: Long, val id: Long) {
        fun getWait(): Long {
            return floorMod(this.id - this.depart, this.id)
        }
    }

    val busses = lines[1].split(",").withIndex().filter { it.value != "x" }
        .map { Bus(it.index.toLong(), it.value.toLong()) }

    println("Part2: ${crt(busses.map { it.getWait() }, busses.map { it.id })}")

}

