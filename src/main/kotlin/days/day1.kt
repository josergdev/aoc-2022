package days

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.asSequence

fun day1part1() = Files.lines(Paths.get("input/1.txt")).asSequence()
    .fold(mutableListOf(mutableListOf<Int>())) { acc, line ->
        if (line.isBlank()) acc.add(mutableListOf())
        else acc.last().add(line.toInt())
        acc
    } .maxOf { it.sum() }

fun day1part2() = Files.lines(Paths.get("input/1.txt")).asSequence()
    .fold(mutableListOf(mutableListOf<Int>())) { acc, line ->
        if (line.isBlank()) acc.add(mutableListOf())
        else acc.last().add(line.toInt())
        acc
    } 
    .map { it.sum() }
    .sortedDescending()
    .take(3)
    .sum()