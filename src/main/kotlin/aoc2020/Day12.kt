package aoc2020

import utils.readAllLines
import java.lang.IllegalStateException
import kotlin.math.abs

data class Position(val heading: Int, val x: Long, val y: Long)
data class ShipAndPoint(val sx: Int, val sy: Int, val px: Int, val py: Int)

fun main() {
    val lines = readAllLines(2020, 12)
    val part1 = lines.fold(Position(90, 0, 0)) { acc, l ->
        val i = l[0]
        val arg = l.substring(1).toInt()
        when (i) {
            'N' -> Position(acc.heading, acc.x, acc.y - arg)
            'S' -> Position(acc.heading, acc.x, acc.y + arg)
            'E' -> Position(acc.heading, acc.x + arg, acc.y)
            'W' -> Position(acc.heading, acc.x - arg, acc.y)
            'L' -> Position((acc.heading + 360 - arg) % 360, acc.x, acc.y)
            'R' -> Position((acc.heading + arg) % 360, acc.x, acc.y)
            'F' -> {
                when (acc.heading) {
                    0 -> Position(acc.heading, acc.x, acc.y - arg)
                    90 -> Position(acc.heading, acc.x + arg, acc.y)
                    180 -> Position(acc.heading, acc.x, acc.y + arg)
                    270 -> Position(acc.heading, acc.x - arg, acc.y)
                    else -> throw IllegalStateException("Bad Direction: ${acc.heading}")
                }
            }
            else -> throw IllegalStateException("Bad Input $l")
        }

    }
    println("Part1: ${abs(part1.x) + abs(part1.y)}")

    val part2 = lines.fold(ShipAndPoint(0, 0, 10, -1)) { acc, l ->
        println(acc)
        val i = l[0]
        val arg = l.substring(1).toInt()
        when (i) {
            'N' -> ShipAndPoint(acc.sx, acc.sy, acc.px, acc.py - arg)
            'S' -> ShipAndPoint(acc.sx, acc.sy, acc.px, acc.py + arg)
            'E' -> ShipAndPoint(acc.sx, acc.sy, acc.px + arg, acc.py)
            'W' -> ShipAndPoint(acc.sx, acc.sy, acc.px - arg, acc.py)
            'F' -> ShipAndPoint(acc.sx + (arg * acc.px), acc.sy + (arg * acc.py), acc.px, acc.py)
            'L' -> rotate(-arg, acc)
            'R' -> rotate(arg, acc)
            else -> throw IllegalStateException("Bad Input $l")
        }

    }
    println("Part2: ${abs(part2.sx) + abs(part2.sy)}")
}

fun rotate(arg: Int, acc: ShipAndPoint): ShipAndPoint {
    return when (val rotation = (360 + arg) % 360) {
        90 -> ShipAndPoint(acc.sx, acc.sy, -acc.py, acc.px)
        180 -> ShipAndPoint(acc.sx, acc.sy, -acc.px, -acc.py)
        270 -> ShipAndPoint(acc.sx, acc.sy, acc.py, -acc.px)
        else -> throw IllegalStateException("Bad rotation $rotation")
    }
}
