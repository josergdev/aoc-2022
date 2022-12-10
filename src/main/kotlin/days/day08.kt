package days

import java.io.File

fun <T> List<List<T>>.transpose(): List<List<T>> = when {
    this.first().isEmpty() -> listOf()
    else -> listOf(this.map { it.first() }) + this.map { it.drop(1) }.transpose()
}

fun day8part1() = File("input/08.txt").readLines()
        .mapIndexed { i, e1 ->  e1.mapIndexed { j, e2 -> (i to j) to e2.digitToInt() }}
        .let { it to it.reversed().map { c -> c.reversed() } }
        .let { (mat, matRev) -> mat + matRev + mat.transpose() + matRev.transpose() }
        .map { it.fold(setOf<Pair<Pair<Int, Int>, Int>>()) { acc, c ->
            if (acc.isNotEmpty() && acc.maxOf { ac -> ac.second } >= c.second) acc
            else acc.plus(c)
        } }
        .flatten().toSet()
        .count()

fun <T> List<T>.takeWhileInclusive(pred: (T) -> Boolean): List<T> {
    var shouldContinue = true
    return takeWhile {
        val result = shouldContinue
        shouldContinue = pred(it)
        result
    }
}

fun day8part2() = File("input/08.txt").readLines()
        .mapIndexed { i, e1 -> e1.mapIndexed { j, e2 -> (i to j) to e2.digitToInt() } }
        .let { it to it.reversed().map { c -> c.reversed() } }
        .let { (mat, matRev) -> mat + matRev + mat.transpose() + matRev.transpose() }
        .fold(mutableMapOf<Pair<Pair<Int, Int>, Int>, List<List<Pair<Pair<Int, Int>, Int>>>>()) { acc, p ->
            p.forEachIndexed { index, pair ->
                if (acc.containsKey(pair)) acc[pair] = acc[pair]!!.plus(listOf(p.subList(index + 1, p.size)))
                else acc[pair] = listOf(p.subList(index + 1, p.size))
            }
            acc
        }.entries
        .map { it.key.second to it.value.map { lp -> lp.map { p -> p.second } } }
        .map { (element, neighbours) -> neighbours.map { it.takeWhileInclusive { tree -> tree < element }.count() } }
        .maxOf { it.reduce { acc, i -> acc * i } }