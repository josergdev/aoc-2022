package days

import java.nio.file.Files
import java.nio.file.Paths

fun startOfMessage(message: String, marker: Int) = message
        .windowed(marker)
        .takeWhile { it.toSet().size != marker }
        .size.plus(marker)

fun day6part1() = startOfMessage(Files.readString(Paths.get("input/06.txt")), 4)

fun day6part2() = startOfMessage(Files.readString(Paths.get("input/06.txt")), 14)