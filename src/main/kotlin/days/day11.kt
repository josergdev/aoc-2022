package days

import java.io.File

fun String.parseMonkeys() = this
    .split("\n\n")
    .map { it.split("\n").map { l -> l.trim() } }
    .map {
        Pair(
            it[0].replace("Monkey ", "").replace(":", "").toLong(),
            Triple(
                it[1].replace("Starting items: ", "").split(",").map { item -> item.trim().toLong() },
                it[2].replace("Operation: new = ", "").parseOp(),
                Triple(
                    it[3].replace("Test: divisible by ", "").toLong(),
                    it[4].replace("If true: throw to monkey ", "").toLong(),
                    it[5].replace("If false: throw to monkey ", "").toLong())))
    }
    .map { (monkey, monkeyData) -> (monkey to (monkeyData.first to 0L)) to (monkey to (monkeyData.second to monkeyData.third)) }
    .let { it.associate { p -> p.first } to it.associate { p -> p.second } }

fun String.parseOp() = when {
    this == "old * old" -> { old: Long -> old * old  }
    this.contains("old +") -> this.replace("old + ", "").toLong().let { { old: Long -> old + it } }
    else -> this.replace("old * ", "").toLong().let { { old: Long -> old * it } }
}

fun executeRound(
    monkeyItems: Map<Long, Pair<List<Long>, Long>>,
    monkeyBehaviour: Map<Long, Pair<(Long) -> Long, Triple<Long, Long, Long>>>,
    worryReduction: (Long) -> Long
) =
    monkeyItems.keys.fold(monkeyItems) { map, monkey -> monkeyInspect(map, monkeyBehaviour, worryReduction, monkey) }

fun monkeyInspect(
    monkeyItems: Map<Long, Pair<List<Long>, Long>>, 
    monkeyBehaviour: Map<Long, Pair<(Long) -> Long, Triple<Long, Long, Long>>>,
    worryReduction: (Long) -> Long,
    monkey: Long
) =
    monkeyItems[monkey]!!.first.fold(monkeyItems) { map , item ->  inspectItem(map, monkeyBehaviour, worryReduction, monkey, item) }

fun inspectItem(
    monkeyItems: Map<Long, Pair<List<Long>, Long>>, 
    monkeyBehaviour: Map<Long, Pair<(Long) -> Long, Triple<Long, Long, Long>>>,
    worryReduction: (Long) -> Long,
    monkey: Long, 
    item: Long
) =
    monkeyItems.toMutableMap().let {
        it[monkey] = it[monkey]!!.first.drop(1) to it[monkey]!!.second + 1
        val newItem = monkeyBehaviour[monkey]!!.first(item)
        val newItemReduced = worryReduction(newItem)
        val newMonkey =
            if ((newItemReduced % monkeyBehaviour[monkey]!!.second.first) == 0L) 
                monkeyBehaviour[monkey]!!.second.second 
            else 
                monkeyBehaviour[monkey]!!.second.third
        it[newMonkey] = it[newMonkey]!!.first.plus(newItemReduced) to it[newMonkey]!!.second
        it.toMap()
    }

fun monkeyBusiness(monkeyItems: Map<Long, Pair<List<Long>, Long>>) = monkeyItems
    .entries.map { it.value.second }
    .sortedDescending()
    .take(2)
    .reduce { acc, act -> acc * act }

fun day11part1() = File("input/11.txt").readText()
    .parseMonkeys()
    .let { (monkeyItems, monkeyBehaviour) ->
        (1 .. 20).fold(monkeyItems) { map, _ ->
            executeRound(map, monkeyBehaviour) { level -> level.div(3)}
        }
    }
    .let { monkeyBusiness(it) }

fun day11part2() = File("input/11.txt").readText()
    .parseMonkeys()
    .let { (monkeyItems, monkeyBehaviour) ->
        (1 .. 10000).fold(monkeyItems) { map, _ ->
            executeRound(map, monkeyBehaviour,
                monkeyBehaviour.values.map { mbv -> mbv.second.first }.reduce(Long::times).let {  
                    { level: Long -> level.mod(it) } 
                }
            )
        }
    }
    .let { monkeyBusiness(it) }