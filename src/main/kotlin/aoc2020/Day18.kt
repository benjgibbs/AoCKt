package aoc2020

import utils.readAllLines
import java.lang.RuntimeException


class Parser(val plus: Int, val mul: Int) {

    private fun precedence(sym: Char): Int {
        return when (sym) {
            '+' -> plus
            '*' -> mul
            '(',')' -> 1
            else -> throw RuntimeException("Unknown $sym")
        }
    }

    private fun isOperator(sym: Char): Boolean {
        return sym in listOf('+', '*', '(', ')')
    }

    private fun toPostfix(infix: String): List<Char> {
        val stack = ArrayDeque<Char>()
        val postFix = mutableListOf<Char>()
        for (i in infix.indices) {
            val sym = infix[i]
            if (isOperator(sym)) {
                when (sym) {
                    '(' -> stack.addLast(sym)
                    ')' -> {
                        while (stack.last() != '(') {
                            postFix.add(stack.removeLast())
                        }
                        stack.removeLast()
                    }
                    else -> {
                        if (stack.isEmpty() || precedence(sym) > precedence(stack.last())) {
                            stack.addLast(sym)
                        } else {
                            while (stack.isNotEmpty() && precedence(sym) <= precedence(stack.last())) {
                                postFix.add(stack.removeLast())
                            }
                            stack.addLast(sym)
                        }
                    }
                }
            } else if (sym != ' ') {
                postFix.add(sym)
            }
        }
        while (stack.isNotEmpty()) {
            postFix.add(stack.removeLast())
        }
        return postFix
    }

    private fun eval(postfix: List<Char>): Long {
        val stack = ArrayDeque<Long>()
        for (c in postfix) {
            if (isOperator(c)) {
                val op2 = stack.removeLast()
                val op1 = stack.removeLast()
                when (c) {
                    '+' -> stack.addLast(op1 + op2)
                    '*' -> stack.addLast(op1 * op2)
                }
            } else {
                stack.addLast(c.toLong() - '0'.toLong())
            }
        }
        return stack.removeLast()
    }

    fun eval(expr: String): Long {
        val postfix = toPostfix(expr)
        //println(postfix)
        return eval(postfix)
    }
}

fun main() {
    val lines = readAllLines(2020, 18)

    val p1 = Parser(2, 2)

    println(p1.eval("1 + 2 * 3 + 4 * 5 + 6"))
    println(p1.eval("1 + (2 * 3) + (4 * (5 + 6))"))
    println(p1.eval("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))

    val part1 = lines.fold(0L) { acc, it -> acc + p1.eval(it) }
    println("Part1: $part1")

    val p2 = Parser(3, 2)

    val part2 = lines.fold(0L) { acc, it -> acc + p2.eval(it) }
    println("Part2: $part2")
}