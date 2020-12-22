package aoc2020

import utils.readAllLines
import java.lang.IllegalStateException


fun main() {
    part1()
    part2()
}

private fun part2() {
    val (p1, p2) = getPlayers()

    val winner = playGame(p1,p2)
    val score = if (winner == 1) {
        calcScore(p1)
    } else {
        calcScore(p2)
    }
    println("Part2: $score")
}


private fun playGame(p1: ArrayDeque<Int>, p2: ArrayDeque<Int>) : Int {
    val previousHands = mutableSetOf<List<Int>>()
    previousHands.add(p1.toList())
    previousHands.add(p2.toList())
    while (p1.isNotEmpty() && p2.isNotEmpty()) {
        val c1 = p1.removeFirst()
        val c2= p2.removeFirst()
        if (p1.size >= c1 && p2.size >= c2){
            val pp1 = ArrayDeque<Int>()
            pp1.addAll(p1.subList(0,c1))
            val pp2 = ArrayDeque<Int>()
            pp2.addAll(p2.subList(0,c2))
            val winner = playGame(pp1, pp2)
            if (winner == 1) {
                p1.addLast(c1)
                p1.addLast(c2)
            } else {
                p2.addLast(c2)
                p2.addLast(c1)
            }
        } else {
            if (c1 > c2) {
                p1.addLast(c1)
                p1.addLast(c2)
            } else if (c2 > c1) {
                p2.addLast(c2)
                p2.addLast(c1)
            } else {
                throw IllegalStateException()
            }
        }
        val p1l = p1.toList()
        val p2l = p2.toList()
        if (previousHands.contains(p1l) || previousHands.contains(p2l)) {
            return 1
        }
        previousHands.add(p1l)
        previousHands.add(p2l)
    }
    if (p1.isNotEmpty()) {
        return 1
    }
    if (p2.isNotEmpty()) {
        return 2
    }
    throw IllegalStateException()
}

private fun part1() {
    val (p1, p2) = getPlayers()

    while (p1.isNotEmpty() && p2.isNotEmpty()) {
        playRound(p1, p2)
    }
    val score = if (p1.isNotEmpty()) {
        calcScore(p1)
    } else {
        calcScore(p2)
    }
    println("Part1: $score")
}

fun calcScore(d: ArrayDeque<Int>) : Long {
    return d.foldRightIndexed(0L) { index, i, acc -> acc + (i * (d.size - index))}
}

fun playRound(p1: ArrayDeque<Int>, p2: ArrayDeque<Int>) {
    val c1 = p1.removeFirst()
    val c2 = p2.removeFirst()

    if (c1 > c2) {
        p1.addLast(c1)
        p1.addLast(c2)
    } else if (c2 > c1) {
        p2.addLast(c2)
        p2.addLast(c1)
    } else {
        throw IllegalStateException()
    }
}

fun getPlayers() : Pair<ArrayDeque<Int>, ArrayDeque<Int>> {
    val player1 = ArrayDeque<Int>()
    val player2 = ArrayDeque<Int>()
    var part = 0
    val input = readAllLines(2020,22)
    for (line in input) {
        if (line.startsWith("Player")){

        } else if (line.isBlank()) {
            part++
        } else {
            val int = line.toInt()
            if (part == 0){
                player1.addLast(int)
            } else {
                player2.addLast(int)
            }
        }
    }
    return Pair(player1, player2)
}
