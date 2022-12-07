package days

import java.nio.file.Files
import java.nio.file.Paths

fun createMapOfSizes(shellOuput: String): Map<List<String>, Int> = shellOuput
    .replaceFirst("\$ ", "")
    .split("\n\$ ")
    .map { 
        when {
            it.startsWith("cd") -> "cd" to listOf(it.replaceFirst("cd ", ""))
            else -> "ls" to it.replaceFirst("ls", "").trim().split("\n")
        } 
    }
    .let { it.mapIndexed { index, pair -> it.take(index + 1).filter { itl -> itl.first == "cd" }.map { itl-> itl.second } to pair  } }
    .filter { it.second.first != "cd" }
    .map { (path, ls) -> path.map { it.first() }.fold(listOf<String>()) { acc, p ->
        when (p) {
            ".." -> acc.dropLast(1)
            else -> acc + p
        }
    } to ls.second }
    .reversed()
    .fold(mutableMapOf()) { map, ls ->
        map[ls.first] = ls.second.sumOf {
            when {
                it.startsWith("dir ") -> map[ls.first + it.replace("dir ", "")]!!
                else -> it.split(" ")[0].toInt()
            }
        }
        map
    }

fun day7part1() = createMapOfSizes(Files.readString(Paths.get("input/7.txt")))
    .values
    .filter { it <= 100000 }
    .sum()

fun day7part2() = createMapOfSizes(Files.readString(Paths.get("input/7.txt")))
    .let { it[listOf("/")]!! to it  }
    .let { (used, map) -> map.entries.sortedBy { it.value }.first { 70000000 - 30000000 - used + it.value > 0 }.value }