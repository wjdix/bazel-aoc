package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

data class BagCount(val count: Int, val name: String) {
    fun times(i: Int): BagCount = this.copy(count = count * i)
}

data class BagSpecification(val name: String = "", val bags: Set<BagCount> = emptySet()) {
    companion object {
        fun parse(string: String): BagSpecification {
            val (name, rest) = string.split(" bags contain ")
            val bags = parseBags(rest.removeSuffix("."))
            return BagSpecification(
                name = name,
                bags = bags,
            )
        }

        private fun parseBags(rest: String): Set<BagCount> =
            if (rest == "no other bags") {
                emptySet()
            } else {
                rest
                    .split(", ")
                    .mapNotNull {
                        "(\\d+) (\\w+) (\\w+) bags?"
                            .toRegex()
                            .matchEntire(it)
                            ?.groupValues
                            ?.let { (_ , count, name1, name2) ->
                                BagCount(name = "$name1 $name2", count = count.toInt())
                            }
                    }
                    .toSet()
            }
    }
}

data class BagGraph(
    val bags: Map<String, Set<String>> = mapOf()
) {
    fun addSpec(spec: BagSpecification): BagGraph =
        if (bags.containsKey(spec.name)) {
            this
        } else {
            val newReachables = spec
                .bags
                .map(BagCount::name)
                .toSet()
            val secondReachables = bags
                .filter { it.key in newReachables }
                .values
                .reduceOrNull(Set<String>::union)
                .orEmpty()
            val allReachables = newReachables + secondReachables
            val newBags = bags
                .plus(Pair(spec.name, allReachables))
                .map { (key, bags) ->
                    if (bags.contains(spec.name)) {
                        Pair(key, bags.plus(allReachables))
                    } else {
                        Pair(key, bags)
                    }
                }
                .toMap()
            this.copy(bags = newBags)
        }
}

object TwentyDaySeven: Solution<BagSpecification, Int>(), Solver {
    override val day: Day
        get() = Day(2020, 7)

    override fun parseInput(input: Sequence<String>): Sequence<BagSpecification> =
        input
            .filter(String::isNotEmpty)
            .map(BagSpecification.Companion::parse)

    override fun partOne(input: Sequence<BagSpecification>): Int =
        input
            .fold(BagGraph()) { graph, spec ->
                graph.addSpec(spec)
            }
            .bags
            .filterValues { it.contains("shiny gold") }
            .count()

    private tailrec fun bagCount(bagCounts: List<BagCount>, bagMap: Map<String, BagSpecification>, sum: Int = 0): Int =
        if (bagCounts.isEmpty()) {
            sum
        } else {
            val first = bagCounts.first()
            val rest = bagCounts.drop(1)
            bagCount(
                bagCounts = rest + bagMap[first.name]?.bags.orEmpty().map { it.times(first.count) },
                bagMap = bagMap,
                sum = sum + first.count
            )
        }

    override fun partTwo(input: Sequence<BagSpecification>): Int =
        bagCount(
            bagCounts = listOf(BagCount(count = 1, name ="shiny gold")),
            bagMap = input.associateBy(BagSpecification::name),
            sum = -1,
        )
}
