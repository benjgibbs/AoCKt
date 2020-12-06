package aoc2020

import utils.readAllLines

data class Rule(val a: Int, val b: Int, val c: Char, val password: String)

fun split(line: String): Rule {
    val parts = line.split(" ", ":", "-").filter { it.isNotBlank() }
    val a = parts[0].toInt()
    val b = parts[1].toInt()
    val check = parts[2].toCharArray()[0]
    return Rule(a, b, check, parts[3])
}

fun part1(rule: Rule): Boolean {
    val count = rule.password.filter { it == rule.c }.count()
    return count in rule.a..rule.b
}

fun part2(rule: Rule): Boolean {
    return (rule.password[rule.a - 1] == rule.c).xor((rule.password[rule.b - 1] == rule.c))
}

fun main() {

    val part1 = readAllLines(2020, 2)
        .map { split(it) }
        .filter { part1(it) }
        .count()

    println("Valid Passwords: $part1")

    val part2 = readAllLines(2020, 2)
        .map { split(it) }
        .filter { part2(it) }
        .count()

    println("Valid Passwords: $part2")

}