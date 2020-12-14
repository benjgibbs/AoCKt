package aoc2020

import utils.ints
import utils.readAllLines
import java.lang.Long.parseLong
import java.lang.Long.toBinaryString

const val BITS = 36


fun applyMask(mask: String, num: Long): Long {
    val intString = toBinaryString(num).padStart(BITS, '0')
    val buffer = MutableList(BITS) { '0' }
    for (i in 0 until BITS) {
        val bit2 = mask[i]
        buffer[i] = if (bit2 == 'X') {
            intString[i]
        } else {
            bit2
        }
    }
    return parseLong(buffer.joinToString(""), 2)
}

fun permute(addr: String): List<String> {
    if (addr.length == 1) {
        return when (addr[0]) {
            'X' -> listOf("0", "1")
            else -> listOf(addr[0].toString())
        }
    }
    val result = mutableListOf<String>()
    val subs = permute(addr.drop(1))
    return when (addr[0]) {
        'X' -> {
            for (s in subs) {
                result.add("1$s")
                result.add("0$s")
            }
            result
        }
        else -> {
            for (s in subs) {
                result.add("${addr[0]}$s")
            }
            result
        }
    }
}

fun generateAddresses(mask: String, addr: Long): List<Long> {
    val intString = toBinaryString(addr).padStart(BITS, '0')
    val buffer = MutableList(BITS) { '0' }
    for (i in 0 until BITS) {
        val bit2 = mask[i]
        buffer[i] = when (bit2) {
            '0' -> {
                intString[i]
            }
            '1' -> {
                '1'
            }
            else -> {
                'X'
            }
        }
    }
    return permute(buffer.joinToString("")).map { parseLong(it, 2) }
}

fun main() {

    println(applyMask("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X", 11))

    val lines = readAllLines(2020, 14)
    var mask = ""
    val memory = mutableMapOf<Long, Long>()
    val memory2 = mutableMapOf<Long, Long>()
    for (line in lines) {
        if (line.startsWith("mask")) {
            mask = line.replace("mask = ", "").trim()
        } else {
            val addressVal = ints(line)
            memory[addressVal[0]] = applyMask(mask, addressVal[1])
            for (addr in generateAddresses(mask, addressVal[0])) {
                memory2[addr] = addressVal[1]
            }

        }
    }
    println("Part1: ${memory.map { it.value }.sum()}")
    println("Part2: ${memory2.map { it.value }.sum()}")
}