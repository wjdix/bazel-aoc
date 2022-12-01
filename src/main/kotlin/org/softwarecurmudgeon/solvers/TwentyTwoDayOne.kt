package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.BlankLineSeparatedSequence
import org.softwarecurmudgeon.common.Day

object TwentyTwoDayOne : Solution<Sequence<Int>, Int>(), Solver {
    override val day: Day
        get() = Day(year = 2022, day = 1)

    override fun parseInput(input: Sequence<String>): Sequence<Sequence<Int>> {
        return BlankLineSeparatedSequence.generate(input)
            .map { it.mapNotNull(String::toIntOrNull) }
    }

    override fun partTwo(input: Sequence<Sequence<Int>>): Int {
        return input.map { it.sum() }.sortedDescending().take(n = 3).sum()
    }

    override fun partOne(input: Sequence<Sequence<Int>>): Int {
        return input.map{ it.sum() }.maxOrNull()!!
    }
}
