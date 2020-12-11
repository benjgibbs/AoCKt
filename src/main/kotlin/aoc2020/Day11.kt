package aoc2020

import utils.readAllLines
import java.lang.Integer.max
import java.lang.Integer.min


fun main() {
    val seats = readAllLines(2020, 11).map { it.toList() }
    println("Part1: ${run(seats, 4, ::countAdjacent)}")
    println("Part2: ${run(seats, 5, ::countSeen)}")
}

private fun run(
    input: List<List<Char>>, criticalOccupation: Int,
    count: (List<List<Char>>, Int, Int) -> Int
): Int {
    var seats = input
    var changed = true

    while (changed) {
        changed = false
        val newSeats = mutableListOf<List<Char>>()
        for (r in seats.indices) {
            val newRow = mutableListOf<Char>()
            for (c in seats[r].indices) {
                val adjacent = count(seats, r, c)
                newRow.add(
                    if (seats[r][c] == 'L' && adjacent == 0) {
                        changed = true
                        '#'
                    } else if (seats[r][c] == '#' && adjacent >= criticalOccupation) {
                        changed = true
                        'L'
                    } else {
                        seats[r][c]
                    }
                )
            }
            newSeats.add(newRow)
        }
        seats = newSeats
    }
    return countOccupied(seats)
}

fun countOccupied(seats: List<List<Char>>): Int {
    return seats.flatten().filter { it == '#' }.count()
}

fun countAdjacent(seats: List<List<Char>>, r: Int, c: Int): Int {
    val rowCount = seats.size
    val colCount = seats[0].size
    var occupied = 0
    for (i in max(r - 1, 0) until min(r + 2, rowCount)) {
        for (j in max(c - 1, 0) until min(c + 2, colCount)) {
            if (seats[i][j] == '#' && !(r == i && c == j)) {
                occupied++
            }
        }
    }
    return occupied
}

fun countSeen(seats: List<List<Char>>, r: Int, c: Int): Int {
    val rowCount = seats.size
    val colCount = seats[0].size

    var count = 0

    var r2 = r - 1
    var c2 = c
    //Up
    while (r2 >= 0) {
        if (seats[r2][c2] == '#') {
            count++
            break
        } else if (seats[r2][c2] == 'L') {
            break
        }
        r2--
    }
    //Down
    r2 = r + 1
    c2 = c
    while (r2 < rowCount) {
        if (seats[r2][c2] == '#') {
            count++
            break
        } else if (seats[r2][c2] == 'L') {
            break
        }
        r2++
    }

    //left
    r2 = r
    c2 = c - 1
    while (c2 >= 0) {
        if (seats[r2][c2] == '#') {
            count++
            break
        } else if (seats[r2][c2] == 'L') {
            break
        }
        c2--
    }

    //right
    r2 = r
    c2 = c + 1
    while (c2 < colCount) {
        if (seats[r2][c2] == '#') {
            count++
            break
        } else if (seats[r2][c2] == 'L') {
            break
        }
        c2++
    }

    // upper left
    r2 = r - 1
    c2 = c - 1
    while (r2 >= 0 && c2 >= 0) {
        if (seats[r2][c2] == '#') {
            count++
            break
        } else if (seats[r2][c2] == 'L') {
            break
        }
        r2--
        c2--
    }

    // upper right
    r2 = r - 1
    c2 = c + 1
    while (r2 >= 0 && c2 < colCount) {
        if (seats[r2][c2] == '#') {
            count++
            break
        } else if (seats[r2][c2] == 'L') {
            break
        }
        r2--
        c2++
    }

    // lower left
    r2 = r + 1
    c2 = c - 1
    while (r2 < rowCount && c2 >= 0) {
        if (seats[r2][c2] == '#') {
            count++
            break
        } else if (seats[r2][c2] == 'L') {
            break
        }
        r2++
        c2--
    }

    //lower right
    r2 = r + 1
    c2 = c + 1
    while (r2 < rowCount && c2 < colCount) {
        if (seats[r2][c2] == '#') {
            count++
            break
        } else if (seats[r2][c2] == 'L') {
            break
        }
        r2++
        c2++
    }

    return count
}