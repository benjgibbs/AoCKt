package aoc2020

import java.lang.Character.getNumericValue as getNumericValue1

fun part1(input: String): String {
    val cups = CircularBuffer(9, input.map { getNumericValue1(it) })
    for (i in 0 until 100) {
        step(cups)
    }
    var s = cups.find(1).next!!
    return (1..8).map { val v = s.value; s = s.next!!; v }.joinToString("")
}

fun part2(input: String): Long {
    val cups = CircularBuffer(1_000_000, input.map { getNumericValue1(it) })
    for (i in 0 until 10_000_000) {
        step(cups)
    }
    val s = cups.find(1)
    return s.next!!.value.toLong() * s.next!!.next!!.value.toLong()
}

fun prev(c: Int, max: Int): Int {
    return if ((c - 1) > 0) c - 1
    else max
}

private fun step(cups: CircularBuffer) {
    //println("Cups: $cups")
    val curVal = cups.currentVal
    val removed: List<Int> = cups.removeCount(3)
    var dest = prev(curVal, cups.sz)
    while (removed.indexOf(dest) != -1) dest = prev(dest, cups.sz)
    //println("curVal=${cups.currentVal}, removed=$removed, dest=$dest")
    cups.addAll(dest, removed)
    cups.increment()
}

fun main() {
    val input = "476138259"
    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")
}

class Node(val value: Int, var next: Node?)

class CircularBuffer(val sz: Int, initial: List<Int>) {
    private val index = mutableMapOf<Int, Node>()
    private var current: Node

    init {
        current = Node(initial.first(), null)
        index[initial.first()] = current
        var p = current
        for (i in 1 until sz) {
            p.next = if (i < initial.size) {
                Node(initial[i], null)
            } else {
                Node(i + 1, null)
            }
            p = p.next!!
            index[p.value] = p
        }
        p.next = current
    }

    val size: Int
        get() {
            return index.size
        }

    fun find(value: Int): Node {
        return index[value]!!
    }

    fun removeCount(count: Int): List<Int> {
        val result = mutableListOf<Int>()
        var p: Node = current
        for (i in 1..count) {
            p = p.next!!
            result.add(p.value)
            index.remove(p.value)
        }
        current.next = p.next
        return result
    }

    fun addAll(value: Int, xs: List<Int>) {
        var p = find(value)
        val end = p.next
        for (x in xs) {
            val n = Node(x, null)
            p.next = n
            index[x] = n
            p = p.next!!
        }
        p.next = end
    }

    fun increment() {
        current = current.next!!
    }

    val currentVal: Int
        get() {
            return current.value
        }

    override fun toString(): String {
        val builder = StringBuilder()
        var it = current
        do {
            if (builder.isNotEmpty()) builder.append(" ")
            builder.append(it.value)
            it = it.next!!
        } while (it != current)
        return builder.toString()
    }
}