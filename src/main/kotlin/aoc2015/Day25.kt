package aoc2015

fun main() {
    val wantRow = 3010
    val wantCol = 3019

    val start = 20151125L
    val fact = 252533L
    val modOf = 33554393L

    assert(calculateCodeNumber(1, 1) == 1)
    assert(calculateCodeNumber(2, 1) == 2)
    assert(calculateCodeNumber(1, 2) == 3)

    val code = calculateCodeNumber(wantRow, wantCol)
    println("Want Code $code")
    var acc: Long = start
    for (i in 2..code) {
        acc = (acc * fact) % modOf
    }
    println("Part1: $acc")
}

private fun calculateCodeNumber(wantRow: Int, wantCol: Int): Int {
    var limit = 1
    var row = 1
    var col = 1
    var code = 1
    while (wantRow != row || wantCol != col) {
        code++
        if (row == 1) {
            limit++
            row = limit
            col = 1
        } else {
            row--
            col++
        }
    }
    return code
}