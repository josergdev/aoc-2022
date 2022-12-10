package days

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.asSequence

fun day3part1() =  Files.lines(Paths.get("input/03.txt")).asSequence()
    .map { it.chunked(it.length / 2) }
    .map { it[0].toSet().intersect(it[1].toSet()).first() }
    .let { it to (('a'..'z') + ('A'..'Z')).zip(1..52).toMap() }
    .let { (items, prio) -> items.map { prio[it]!! } }
    .sum()

fun day3part2() = Files.lines(Paths.get("input/03.txt")).asSequence()
    .map { it.toSet() }
    .chunked(3)
    .map { it[0].intersect(it[1]).intersect(it[2]).first() }
    .let { it to (('a'..'z') + ('A'..'Z')).zip(1..52).toMap() }
    .let { (items, prio) -> items.map { prio[it]!! } }
    .sum()