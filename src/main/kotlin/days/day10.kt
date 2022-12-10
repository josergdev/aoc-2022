package days

import java.io.File

fun String.parse() = when(this) {
    "noop" -> sequence{ yield { (cycle, x): Pair<Int, Int> -> cycle + 1 to x } }
    else -> this.replace("addx ", "").toInt().let {
        sequence{
            yield { (cycle, x): Pair<Int, Int> -> cycle + 1 to x }
            yield { (cycle, x): Pair<Int, Int> -> cycle + 1 to x + it }
        }
    }
}

fun day10part1() = File("input/10.txt").readLines()
    .flatMap { it.parse() }
    .scan(1 to 1) { acc, f -> f(acc) }
    .filter { (cycle, _) -> cycle in 20..220 step 40 }
    .sumOf { (cycle, x) -> cycle * x }

fun day10part2() = File("input/10.txt").readLines()
    .flatMap { it.parse() }.asSequence()
    .scan(0 to 1) { acc, f -> f(acc) }
    .map { (cycle, x) -> cycle.mod(40) to x }
    .map { (pointer, x ) -> if (x in (pointer - 1 .. pointer + 1)) '#' else '.' }
    .chunked(40)
    .map { it.joinToString(" ") }
    .joinToString("\n")
