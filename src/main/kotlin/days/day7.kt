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
    .let { it.scan(listOf<String>()) { acc, (command, dirOrStdout) -> 
            when(command) {
                "cd" -> when {
                    dirOrStdout.first() == ".." -> acc.dropLast(1)
                    else -> acc + dirOrStdout.first()
                }
                else -> acc
            }
        }
        .zip(it)
    }
    .filter { (_, command) -> command.first != "cd" }
    .map { (path, command) -> path to command.second }
    .reversed()
    .fold(mutableMapOf()) { map, (path, ls) ->
        map[path] = ls.sumOf {
            when {
                it.startsWith("dir ") -> map[path + it.replace("dir ", "")]!!
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