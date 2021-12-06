package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

fun spawn(days: Int): List<Int> =
    if (days == 0) {
        listOf(8, 6)
    } else {
        listOf(days - 1)
    }

object TwentyOneDaySix: Solution<Int, Long>(), Solver {
    override val day: Day
        get() = Day(2021, 6)

    override fun parseInput(input: Sequence<String>): Sequence<Int> =
        input
            .filter(String::isNotEmpty)
            .joinToString("")
            .split(",")
            .mapNotNull(String::toIntOrNull)
            .asSequence()

    private fun calculateForDays(input: Sequence<Int>, days: Int): Long {
        val grouped: Map<Int, Long> = input
            .groupBy { it }
            .map { (k, v) -> Pair(k, v.count().toLong()) }
            .toMap()
        return 0.until(days).fold(grouped) { fish, _ ->
            fish.flatMap(::spawnMap)
                .groupBy { it.first }
                .map { (k, v) -> Pair(k, v.sumOf { it.second } )}
                .toMap()
        }
            .values
            .sum()

    }

    override fun partOne(input: Sequence<Int>): Long =
        calculateForDays(input, 80)

    override fun partTwo(input: Sequence<Int>): Long =
        calculateForDays(input, 256)

    private fun spawnMap(pair: Map.Entry<Int, Long>): List<Pair<Int, Long>> =
        if (pair.key == 0) {
            listOf(
                Pair(6, pair.value),
                Pair(8, pair.value)
            )
        } else {
            listOf(
                Pair(pair.key - 1, pair.value)
            )
        }
    }
