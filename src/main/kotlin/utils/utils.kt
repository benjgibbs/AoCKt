package utils

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