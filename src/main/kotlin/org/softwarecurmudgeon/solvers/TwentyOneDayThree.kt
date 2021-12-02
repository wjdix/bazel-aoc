package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

object TwentyOneDayThree: Solution<Int, Int>(), Solver {
    override val day: Day
        get() = Day(2021, 3)

    override fun parseInput(input: Sequence<String>): Sequence<Int>  =
        input
            .filter(String::isNotEmpty)
            .mapNotNull(String::toIntOrNull)

    override fun partOne(input: Sequence<Int>): Int = 4

    override fun partTwo(input: Sequence<Int>): Int = 5
}
