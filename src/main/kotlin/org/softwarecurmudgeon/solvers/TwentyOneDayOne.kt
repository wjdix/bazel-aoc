package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

object TwentyOneDayOne: Solution<Int, Int>(), Solver {
    override val day: Day
        get() = Day(2021, 1)

    override fun parseInput(input: Sequence<String>): Sequence<Int> =
        input.mapNotNull(String::toIntOrNull)

    override fun partOne(input: Sequence<Int>): Int =
        input
            .zipWithNext()
            .count { (a, b) -> a < b}

    override fun partTwo(input: Sequence<Int>): Int =
        input
            .windowed(3)
            .map { (a, b, c) -> a + b + c }
            .zipWithNext()
            .count{ (a, b) -> a < b}
}
