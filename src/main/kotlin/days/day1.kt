package days

import java.nio.file.Files
import java.nio.file.Paths

fun day1part1() = Files.readString(Paths.get("input/1.txt"))
    .split("\n\n")
    .map { it.split("\n").map { s -> s.toInt() } }
    .maxOf { it.sum() }

fun day1part2() = Files.readString(Paths.get("input/1.txt"))
    .split("\n\n").asSequence()
    .map { it.split("\n").map { s -> s.toInt() } }
    .map { it.sum() }
    .sortedDescending()
    .take(3)
    .sum()