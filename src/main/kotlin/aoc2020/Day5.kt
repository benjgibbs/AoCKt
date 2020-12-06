package aoc2020

import utils.readAllLines

fun calcRow(line: String) : Int {
    var rl = 0
    var ru = 127
    for (i in 0 until 6) {
        if (line[i] == 'F') {
            ru -= (ru-rl+1)/2
        } else {
            rl += (1 + (ru-rl)/2)
        }
        //println("${line[i]}, $rl, $ru")
    }
    return if(line[6] == 'F') rl else ru
}

fun calcCol(line: String) : Int {
    var cl = 0
    var cu = 7
    for (i in 7 until 9) {
        if (line[i] == 'L') {
            cu -= (cu-cl+1)/2
        } else {
            cl += (1 + (cu-cl-1)/2)
        }
        //println("${line[i]}, $cl, $cu")
    }
    return if(line[9] == 'L') cl else cu
}


fun main() {
//    println("Test Row: ${calcRow("FBFBBFFRLR")}")
//    println("Test Col: ${calcCol("FBFBBFFRLR")}")

    val max = readAllLines(2020, 5).map { line ->
        calcRow(line) * 8 +  calcCol(line)
    }.maxOrNull()
    println("Part1: $max")

    val all = readAllLines(2020, 5).map { line ->
        calcRow(line) * 8 +  calcCol(line)
    }.sorted()

    for (i in 13..822){
        if (! all.contains(i)){
            println("Part2: $i")
        }
    }

}