package utils

import java.lang.Long.max
import java.lang.Long.min
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

fun readAllLines(year: Int, day:Int) : List<String> {
    var path = Paths.get("src/main/resources/aoc$year/Day$day.txt")

    if (!Files.exists(path)) {
        val url = URL("https", "adventofcode.com", "/$year/day/$day/input")
        println("No local file: $path, reading from $url")

        val props = Properties()
        props.load(Files.newInputStream(Paths.get("src/main/resources/secret.properties")))
        val session = props.get("session")

        val conn = url.openConnection()
        conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.67 Safari/537.36 Edg/87.0.664.47")
        conn.addRequestProperty("Cookie", "session=$session")
        val bytes = conn.getInputStream().readAllBytes()
        val input = String(bytes, Charsets.UTF_8)
        println(input)

        val folder = Paths.get("src/main/resources/aoc$year")
        if (!Files.exists(folder)){
            Files.createDirectories(folder)
        }

        path = Files.createFile(path)
        Files.writeString(path, input)
    }

    return Files.readAllLines(path)
}

fun longs(s : String) : List<Long> {
    val intRegex = """-?\d+""".toRegex()
    return intRegex.findAll(s).map { it.groupValues[0].toLong() }.toList()
}

fun ints(s : String) : List<Int> {
    val intRegex = """-?\d+""".toRegex()
    return intRegex.findAll(s).map { it.groupValues[0].toInt() }.toList()
}

fun reals(s: String): List<Double> {
    val realRegex = """-?\d+(.\d+)""".toRegex()
    return realRegex.findAll(s).map { it.groupValues[0].toDouble() }.toList()
}

fun euclid(x: Long, y: Long) : List<Long> {

    var a = max(x,y)
    var b = min(x,y)

    var q1 = a/b
    var r1 = a%b
    while (r1 != 0L){
        a = b
        b = r1
        q1 = a/b
        r1 = a%b
    }
    return listOf(b, q1, r1)
}

fun crt(ais: List<Long>, pis: List<Long>): Long {
    fun u(factor: Long, prime: Long) : Long {
        var result = 0L
        while ((factor * result) % prime != 1L ){
            result++
        }
        return result
    }

    val N = pis.fold(1L) { acc, i -> acc * i }
    var result = 0L
    for (i in ais.indices) {
        val ai = ais[i]
        val pi = pis[i]
        val ni = N/pi
        val ui = u(ni,pi)
        result += (ai * ni * ui)
    }
    return result % N
}