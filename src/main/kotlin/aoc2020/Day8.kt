package aoc2020

import utils.readAllLines
import java.lang.IllegalStateException


data class Result(
    val complete: Boolean,
    val pc: Int,
    val acc: Int,
    val seen: Set<Int>,
)

fun run(code: List<String>): Result {
    var acc = 0
    var pc = 0
    val seen = mutableSetOf<Int>()
    while (pc < code.size) {
        if (seen.contains(pc)) {
           return Result(false, pc, acc, seen)
        }
        seen.add(pc)
        val op = code[pc].split(" ")
        val p = op[1].toInt()
        //println("pc: $pc, op: $op, acc: $acc")
        pc = when (op[0]) {
            "nop" -> {
                pc + 1
            }
            "acc" -> {
                acc += p
                pc + 1
            }
            "jmp" -> {
                pc + p
            }
            else -> throw IllegalStateException("Unknown instruction: $op")
        }
    }
    return Result(true, pc, acc, seen)
}

fun main() {
    val code = readAllLines(2020, 8)

    val part1 = run(code)
    println("part1: ${part1.acc}")

    val nops = part1.seen.filter{ i-> code[i].startsWith("nop")}

    for (noop in nops) {
        val codeCopy = code.toMutableList()
        codeCopy[noop] = codeCopy[noop].replace("nop", "jmp")
        val result = run(codeCopy)
        if (result.complete){
            println("Part2: ${result.acc}")
            break
        }
    }

    val jmps = part1.seen.filter{i-> code[i].startsWith("jmp")}

    for (jmp in jmps) {
        val codeCopy = code.toMutableList()
        codeCopy[jmp] = codeCopy[jmp].replace("jmp", "nop")
        val result = run(codeCopy)
        if (result.complete){
            println("Replaced: ${jmp}")
            println("Part2: ${result.acc}")
            break
        }
    }
}