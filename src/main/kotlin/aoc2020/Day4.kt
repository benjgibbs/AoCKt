package aoc2020

import utils.readAllLines

fun main() {

    val required = setOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

    var keys = mutableSetOf<String>()
    var valid  = 0
    readAllLines(2020, 4).forEach {
        if (it.isEmpty()) {
            if (keys == required) {
                valid++
            }
            keys = mutableSetOf()
        }
        val allParts = it.split(":", " ").filter{i -> i.isNotEmpty()}.toSet()
        val intersect = allParts.intersect(required)
        keys.addAll(intersect)
    }
    if (keys == required) {
        valid++
    }
    println("Part1: $valid")

    valid = 0

    readAllLines(2020, 4).forEach {
        if (it.isEmpty()) {
            if (keys == required) {
                valid++
            }
            keys = mutableSetOf()
        }
        val allParts = it.split(" ").filter{i -> i.isNotEmpty()}
        for (part in allParts){
            val kv = part.split(":")
            when(kv[0]) {
                "byr" -> {
                    if (kv[1].toInt() in 1920..2002) {
                        keys.add(kv[0])
                    }
                }
                "iyr" -> {
                    if (kv[1].toInt() in 2010..2020) {
                        keys.add(kv[0])
                    }
                }
                "eyr" -> {
                    if (kv[1].toInt() in 2020..2030) {
                        keys.add(kv[0])
                    }
                }
                "hgt" -> {
                    if (kv[1].endsWith("cm")) {
                        val cm = kv[1].substringBefore("c").toInt()
                        if (cm in 150..193){
                            keys.add(kv[0])
                        }
                    }
                    if (kv[1].endsWith("in")) {
                        val inch = kv[1].substringBefore("i").toInt()
                        if (inch in 59..76){
                            keys.add(kv[0])
                        }
                    }
                }
                "hcl" -> {
                    if (kv[1][0] == '#') {
                        val re = """[0-9a-f]{6}""".toRegex()
                        if (re.matches(kv[1].substring(1))){
                            keys.add(kv[0])
                        }
                    }
                }
                "ecl" -> {
                    if (kv[1] in setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")){
                        keys.add(kv[0])
                    }
                }
                "pid" -> {
                    val re = """[0-9]{9}""".toRegex()
                    if (re.matches(kv[1])){
                        keys.add(kv[0])
                    }
                }

            }
        }
        val intersect = allParts.intersect(required)
        keys.addAll(intersect)
    }
    if (keys == required) {
        valid++
    }
    println("Part2: $valid")
}