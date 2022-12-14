package days

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.asSequence

sealed interface Option
object Rock : Option
object Paper : Option
object Scissors : Option

fun String.parseOption() = when (this) {
    "A", "X" -> Rock
    "B", "Y" -> Paper
    "C", "Z" -> Scissors
    else -> throw IllegalArgumentException(this)
}

fun Pair<Option, Option>.matchPoints() = when(this) {
    Pair(Scissors, Rock), Pair(Rock, Paper), Pair(Paper, Scissors) -> 6
    else -> when (this.first) {
        this.second -> 3
        else -> 0
    }
}

fun Option.selectionPoints() = when(this) {
    Rock -> 1
    Paper -> 2
    Scissors -> 3
}

fun day2Part1() = Files.lines(Paths.get("input/02.txt")).asSequence()
    .map { it.split(" ") }
    .map { it[0].parseOption() to it[1].parseOption() }
    .map { it.matchPoints() + it.second.selectionPoints() }
    .sum()

sealed interface Result
object Win: Result
object Draw: Result
object Lose: Result

fun String.parseResult() = when (this) {
    "X" -> Lose
    "Y" -> Draw
    "Z" -> Win
    else -> throw IllegalArgumentException(this)
}

fun neededFor(result: Result, option: Option) = when (result) {
    Lose -> when (option) {
        Rock -> Scissors
        Paper -> Rock
        Scissors -> Paper
    }
    Draw -> option
    Win -> when (option) {
        Rock -> Paper
        Paper -> Scissors
        Scissors -> Rock
    }
}

fun day2part2() = Files.lines(Paths.get("input/02.txt")).asSequence()
    .map { it.split(" ") }
    .map { it[0].parseOption() to it[1].parseResult() }
    .map { (option, result) -> option to neededFor(result, option) }
    .map { it.matchPoints() + it.second.selectionPoints() }
    .sum()