package days

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.asSequence

fun day3part1() =  Files.lines(Paths.get("input/3.txt")).asSequence()
    .map { it.toList() }
    .map { it.chunked(it.size / 2) }
    .map { it[0].intersect(it[1].toSet()).first() }
    .let { it to (CharRange('a', 'z') + CharRange('A', 'Z')).zip(IntRange(1, 52)).toMap() }
    .let { (items, prio) -> items.map { prio[it]!! } }
    .sum()

fun day3part2() = Files.lines(Paths.get("input/3.txt")).asSequence()
    .map { it.toSet() }
    .windowed(3, 3)
    .map { it[0].intersect(it[1]).intersect(it[2]).first() }
    .let { it to (CharRange('a', 'z') + CharRange('A', 'Z')).zip(IntRange(1, 52)).toMap() }
    .let { (items, prio) -> items.map { prio[it]!! } }
    .sum()