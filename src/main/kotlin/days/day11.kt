package days

import java.io.File

fun String.parseMonkeys() = this
    .split("\n\n")
    .map { it.split("\n").map { l -> l.trim() } }
    .map {
        Pair(
            it[0].replace("Monkey ", "").replace(":", "").toInt(),
            Triple(
                it[1].replace("Starting items: ", "").split(",").map { item -> item.trim().toInt() },
                it[2].replace("Operation: new = ", "").parseOp(),
                Triple(
                    it[3].replace("Test: divisible by ", "").toInt(),
                    it[4].replace("If true: throw to monkey ", "").toInt(),
                    it[5].replace("If false: throw to monkey ", "").toInt()).parseTest()))
    }
    .map { (monkey, monkeyData) -> (monkey to (monkeyData.first to 0)) to (monkey to (monkeyData.second to monkeyData.third)) }
    .let { it.associate { p -> p.first } to it.associate { p -> p.second } }

fun String.parseOp() = when {
    this == "old * old" -> { old: Int -> old * old  }
    this.contains("old +") -> this.replace("old + ", "").toInt().let { { old: Int -> old + it } }
    else -> this.replace("old * ", "").toInt().let { { old: Int -> old * it } }
}

fun Triple<Int, Int, Int>.parseTest() = { item : Int -> if (item % this.first == 0) this.second else this.third }

fun executeRound(monkeyItems: Map<Int, Pair<List<Int>, Int>>, monkeyBehaviour: Map<Int, Pair<(Int) -> Int, (Int) -> Int>> ) =
    monkeyItems.keys.fold(monkeyItems) { map, monkey -> monkeyInspect(map, monkeyBehaviour, monkey) }

fun monkeyInspect(monkeyItems: Map<Int, Pair<List<Int>, Int>>, monkeyBehaviour: Map<Int, Pair<(Int) -> Int, (Int) -> Int>>, monkey: Int) =
    monkeyItems[monkey]!!.first.fold(monkeyItems) { map , item ->  inspectItem(map, monkeyBehaviour, monkey, item) }

fun inspectItem(monkeyItems: Map<Int, Pair<List<Int>, Int>>, monkeyBehaviour: Map<Int, Pair<(Int) -> Int, (Int) -> Int>>, monkey: Int, item: Int) =
    monkeyItems.toMutableMap().let {
        it[monkey] = it[monkey]!!.first.drop(1) to it[monkey]!!.second + 1
        val newItem = monkeyBehaviour[monkey]!!.first(item)
        val newItemDivided = newItem.div(3)
        val newMonkey = monkeyBehaviour[monkey]!!.second(newItemDivided)
        it[newMonkey] = it[newMonkey]!!.first.plus(newItemDivided) to it[newMonkey]!!.second
        it.toMap()
    }

fun monkeyBusiness(monkeyItems: Map<Int, Pair<List<Int>, Int>>) = monkeyItems
    .entries.map { it.value.second }
    .sortedDescending()
    .take(2)
    .reduce { acc, act -> acc * act }

fun day11part1() = File("input/11.txt").readText()
    .parseMonkeys()
    .let { (monkeyItems, monkeyBehaviour) ->
        (1 .. 20).fold(monkeyItems) { map, _ ->
            executeRound(map, monkeyBehaviour)
        }
    }
    .let { monkeyBusiness(it) }