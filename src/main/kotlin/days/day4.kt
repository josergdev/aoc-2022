package days

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.asSequence

fun <T : Comparable<T>> ClosedRange<T>.containsAll(elements: Iterable<T>) = elements.all(this::contains)

fun day4part1() = Files.lines(Paths.get("input/4.txt")).asSequence()
    .map { it.split(',', '-').map(String::toInt) }
    .filter { IntRange(it[0], it[1]).containsAll(setOf(it[2], it[3])) || IntRange(it[2], it[3]).containsAll(setOf(it[0], it[1])) }
    .count()

fun <T : Comparable<T>> ClosedRange<T>.containsSome(elements: Iterable<T>) = elements.any(this::contains)

fun day4part2() = Files.lines(Paths.get("input/4.txt")).asSequence()
    .map { it.split(',', '-').map(String::toInt) }
    .filter { IntRange(it[0], it[1]).containsSome(IntRange(it[2], it[3])) }
    .count()