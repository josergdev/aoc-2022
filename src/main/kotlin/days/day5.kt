package days

import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

fun parseCrates(crates: String) = crates.split("\n").dropLast(1)
    .map { it.windowed(3,4) }
    .map { it.map { s -> s.trim().replace("[", "").replace("]", "") } }
    .reversed()
    .fold(mutableMapOf<Int, Stack<String>>()) { acc, line ->
        line.forEachIndexed { index, crate ->
            if (crate.isNotBlank())  
                acc.getOrDefault(index + 1, Stack())
                    .also { it.push(crate) }
                    .also { acc[index + 1] = it }
        }
        acc
    }

fun parseMoves(moves: String) = moves.split("\n")
    .map { it.replace("move ", "").replace(" from ", " ").replace(" to ", " ") }
    .map { it.split(" ").map(String::toInt) }

fun MutableMap<Int, Stack<String>>.move(quantity: Int, from: Int, to: Int ): MutableMap<Int, Stack<String>> {
    (1..quantity).forEach { _ -> this[to]!!.push(this[from]!!.pop()) }
    return this
}

fun day5part1() = Files.readString(Paths.get("input/5.txt")).split("\n\n")
    .let { parseCrates(it[0]) to parseMoves(it[1]) }
    .let { (crates, moves) -> moves.fold(crates) { acc, move -> acc.move(move[0], move[1], move[2]) } }
    .entries.sortedBy { it.key }
    .joinToString("") { it.value.peek() }

fun MutableMap<Int, Stack<String>>.move2(quantity: Int, from: Int, to: Int ): MutableMap<Int, Stack<String>> {
    (1..quantity).fold(mutableListOf<String>()) { acc, _ -> 
        acc.add(this[from]!!.pop()) 
        acc
    } 
    .reversed().forEach { this[to]!!.push(it) }
    return this
}

fun day5part2() = Files.readString(Paths.get("input/5.txt")).split("\n\n")
    .let { parseCrates(it[0]) to parseMoves(it[1]) }
    .let { (crates, moves) -> moves.fold(crates) { acc, move -> acc.move2(move[0], move[1], move[2]) } }
    .entries.sortedBy { it.key }
    .joinToString("") { it.value.peek() }