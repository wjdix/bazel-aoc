package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

object EighteenDayOne: Solution<Int, Int>(), Solver {
    override val day: Day
        get() = Day(2018, 1)

    override fun parseInput(input: Sequence<String>): Sequence<Int> =
        input
            .filter(String::isNotEmpty)
            .map(String::trim)
            .mapNotNull(String::toIntOrNull)

    override fun partOne(input: Sequence<Int>): Int =
        input.reduce(Int::plus)

    override fun partTwo(input: Sequence<Int>): Int =
        generateSequence(input) { it }
            .flatten()
            .runningFold(Pair(0, setOf<Int>())) { (current, visited), change ->
                Pair(current + change, visited.plus(current))
            }
            .mapIndexed{ i, it ->
                if (i % 100000 == 0) {
                    println(it)
                    it
                } else {
                    it
                }
            }
            .first { (a, b) -> a in b}
            .first
}
