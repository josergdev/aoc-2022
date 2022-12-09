package days

import java.io.File

fun <T> transpose(elements: List<List<T>>): List<List<T>> = when {
    elements.first().isEmpty() -> listOf()
    else -> listOf(elements.map { it.first() }) + transpose(elements.map { it.drop(1) })
}

fun <T> List<T>.takeWhileInclusive(pred: (T) -> Boolean): List<T> {
    var shouldContinue = true
    return takeWhile {
        val result = shouldContinue
        shouldContinue = pred(it)
        result
    }
}

fun day8part1() = File("input/8.txt").readLines()
        .mapIndexed { i, e1 ->  e1.mapIndexed { j, e2 -> (i to j) to e2.digitToInt() }}
        .let { it to it.reversed().map { c -> c.reversed() } }
        .let { (mat, matRev) -> mat + matRev + transpose(mat) + transpose(matRev) }
        .map { it.fold(setOf<Pair<Pair<Int,Int>,Int>>()) { acc, c ->
            if (acc.isNotEmpty() && acc.maxOf { ac -> ac.second } >= c.second) acc
            else acc.plus(c)
        } }
        .flatten().toSet()
        .count()

fun day8part2() = File("input/8.txt").readLines()
        .mapIndexed { i, e1 -> e1.mapIndexed { j, e2 -> (i to j) to e2.digitToInt() } }
        .let { it to it.reversed().map { c -> c.reversed() } }
        .let { (mat, matRev) -> mat + matRev + transpose(mat) + transpose(matRev) }
        .fold(mutableMapOf<Pair<Pair<Int, Int>, Int>, List<List<Pair<Pair<Int, Int>, Int>>>>()) { acc, p ->
            p.forEachIndexed { index, pair ->
                if (acc.containsKey(pair)) acc[pair] = acc[pair]!!.plus(listOf(p.subList(index + 1, p.size)))
                else acc[pair] = listOf(p.subList(index + 1, p.size))
            }
            acc
        }.entries
        .map { it.key to it.value.map { lp -> lp.map { p -> p.second } } }
        .map { (element, neighbours) -> neighbours.map { it.takeWhileInclusive { tree -> tree < element.second }.count() } }
        .maxOf { it.reduce { acc, i -> acc * i } }