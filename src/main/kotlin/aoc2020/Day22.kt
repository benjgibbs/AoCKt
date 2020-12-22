package aoc2020

import utils.ints
import utils.readAllLines
import java.lang.IllegalStateException

fun main() {
    part1()
    part2()
}

private fun part2() {
    val (p1, p2) = getPlayers()
    val winner = playGame(p1, p2)
    val score = calcScore(if (winner == 1) p1 else p2)
    println("Part2: $score")
}

private fun playGame(p1: ArrayDeque<Int>, p2: ArrayDeque<Int>): Int {
    val previousHands = mutableSetOf(p1.toList(), p2.toList())
    while (p1.isNotEmpty() && p2.isNotEmpty()) {
        val c1 = p1.removeFirst()
        val c2 = p2.removeFirst()
        if (p1.size >= c1 && p2.size >= c2) {
            val winner = playGame(copy(p1, c1), copy(p2, c2))
            if (winner == 1) {
                p1.addLast(c1)
                p1.addLast(c2)
            } else {
                p2.addLast(c2)
                p2.addLast(c1)
            }
        } else {
            adjustForWin(c1, c2, p1, p2)
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

private fun copy(
    p1: ArrayDeque<Int>,
    count: Int
): ArrayDeque<Int> {
    val pp1 = ArrayDeque<Int>()
    pp1.addAll(p1.subList(0, count))
    return pp1
}

private fun part1() {
    val (p1, p2) = getPlayers()
    while (p1.isNotEmpty() && p2.isNotEmpty()) {
        val c1 = p1.removeFirst()
        val c2 = p2.removeFirst()
        adjustForWin(c1, c2, p1, p2)
    }
    val score = calcScore(if (p1.isNotEmpty()) p1 else p2)
    println("Part1: $score")
}

private fun calcScore(d: ArrayDeque<Int>): Long {
    return d.foldIndexed(0L) { index, acc, i -> acc + (i * (d.size - index)) }
}

private fun adjustForWin(
    c1: Int,
    c2: Int,
    p1: ArrayDeque<Int>,
    p2: ArrayDeque<Int>
) {
    when {
        c1 > c2 -> {
            p1.addLast(c1)
            p1.addLast(c2)
        }
        c2 > c1 -> {
            p2.addLast(c2)
            p2.addLast(c1)
        }
        else -> {
            throw IllegalStateException()
        }
    }
}

private fun getPlayers(): Pair<ArrayDeque<Int>, ArrayDeque<Int>> {
    val player1 = ArrayDeque<Int>()
    val player2 = ArrayDeque<Int>()
    var player = 0
    val input = readAllLines(2020, 22)
    for (line in input) {
        if (line.startsWith("Player")) {
            player = ints(line)[0]
        } else if (line.isBlank()) {
            player++
        } else {
            val int = line.toInt()
            if (player == 1
            ) {
                player1.addLast(int)
            } else {
                player2.addLast(int)
            }
        }
    }
    return Pair(player1, player2)
}
