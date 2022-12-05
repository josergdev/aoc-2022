package days

import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

fun parseCrates(crates: String) = crates.split("\n").dropLast(1)
    .map { it.windowed(3,4) }
    .map { it.map { s -> s.trim(' ', '[', ']') } }
    .foldRight(mapOf<Int, Stack<String>>()) { line, cratesAcc ->
        line.foldIndexed(cratesAcc) { index, acc, crate -> 
            if (crate.isNotBlank()) 
                acc.toMutableMap().let { map -> 
                    map.getOrDefault(index + 1, Stack())
                        .also { it.push(crate) }
                        .also { map[index + 1] = it }
                    map.toMap()
                }
            else acc
        }
    }

fun parseMoves(moves: String) = moves.split("\n")
    .map { it.replace("move ", "").replace(" from ", " ").replace(" to ", " ") }
    .map { it.split(" ").map(String::toInt) }

fun Map<Int, Stack<String>>.move(quantity: Int, from: Int, to: Int ) =
    this.toMutableMap().let {  
        (1..quantity).forEach { _ -> it[to]!!.push(it[from]!!.pop()) }
        it.toMap()
    }

fun day5part1() = Files.readString(Paths.get("input/5.txt")).split("\n\n")
    .let { parseCrates(it[0]) to parseMoves(it[1]) }
    .let { (crates, moves) -> moves.fold(crates) { acc, move -> acc.move(move[0], move[1], move[2]) } }
    .entries.sortedBy { it.key }
    .joinToString("") { it.value.peek() }

fun Map<Int, Stack<String>>.move2(quantity: Int, from: Int, to: Int ) = 
    this.toMutableMap().let {
        (1..quantity)
            .fold(listOf<String>()) { acc, _ -> (acc + it[from]!!.pop()) }
            .reversed().forEach { crate -> it[to]!!.push(crate) }
        it.toMap()
    }

fun day5part2() = Files.readString(Paths.get("input/5.txt")).split("\n\n")
    .let { parseCrates(it[0]) to parseMoves(it[1]) }
    .let { (crates, moves) -> moves.fold(crates) { acc, move -> acc.move2(move[0], move[1], move[2]) } }
    .entries.sortedBy { it.key }
    .joinToString("") { it.value.peek() }