package aoc2020

import utils.ints
import utils.readAllLines

fun main() {
    val lines = readAllLines(2020, 13)
    var ints = ints(lines.joinToString(","))
    //ints = listOf(939, 7, 13, 59, 31, 19)
    val earliest = ints[0]
    var minWait = Int.MAX_VALUE
    var id = Int.MAX_VALUE
    for (i in ints.drop(1)) {
        var depart = i
        while (depart < earliest) {
            depart+= i
        }
        val wait = ( depart - earliest)
        if (wait < minWait) {
            println("id: $i, wait: $wait")
            minWait = wait.toInt()
            id = i.toInt()
        }
    }
    //  not 19
    println("Part1: ${id * minWait}")

    var input2 = lines[1].split(",")
    input2 = "7,13,x,x,59,x,31,1".split(",")
    val facts = mutableListOf<Int>()
    for (i in 0 until input2.size) {
        if (input2[i] != "x") {
            facts.add(i+ input2[i].toInt())
        }
    }
    println("facts: $facts")
    println("Part2: ${facts.fold(1){a,b -> a*b}}")

    //to low: 626302976

    var busses = (0..input2.size).zip(input2).filter { it.second != "x" }

}