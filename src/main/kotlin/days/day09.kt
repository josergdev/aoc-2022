package days

import java.io.File
import kotlin.math.abs
import kotlin.math.max

fun Pair<Int, Int>.moveHead(cmd: Char) = when (cmd) {
    'U' -> this.first to this.second + 1
    'R' -> this.first + 1 to this.second
    'D' -> this.first to this.second - 1
    'L' -> this.first - 1 to this.second
    else -> throw IllegalArgumentException()
}

fun Pair<Int, Int>.moveTail(head: Pair<Int, Int>) = when {
    this.isTouching(head) -> this
    this.second == head.second -> when {
        head.first < this.first -> this.first - 1 to this.second
        else -> this.first + 1 to this.second
    }
    this.first == head.first -> when {
        head.second < this.second -> this.first to this.second - 1
        else -> this.first to this.second + 1
    }
    else -> when {
        head.second < this.second -> when {
            head.first < this.first -> this.first - 1 to this.second - 1
            else -> this.first + 1 to this.second - 1
        }
        else -> when {
            head.first < this.first -> this.first - 1 to this.second + 1
            else -> this.first + 1 to this.second + 1
        }
    }
}

fun Pair<Int, Int>.isTouching(point: Pair<Int, Int>) = max(abs(point.first - this.first), abs(point.second - this.second)) <= 1

fun tailPositions(commands: List<String>, knotsSize: Int) = commands
    .map { it.split(" ") }
    .map { it[0].repeat(it[1].toInt()) }
    .flatMap { it.toList() }
    .scan(List(knotsSize) { 0 to 0 }) { knots, cmd ->
        knots.drop(1).fold(listOf(knots.first().moveHead(cmd))) { acc, knot ->
            acc + knot.moveTail(acc.last())
        }
    }
    .map { it.last() }
    .toSet()
    .size

fun day9part1() = tailPositions(File("input/09.txt").readLines(), 2)

fun day9part2() = tailPositions(File("input/09.txt").readLines(), 10)
