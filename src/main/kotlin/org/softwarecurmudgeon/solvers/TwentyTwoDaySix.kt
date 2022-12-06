package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

object TwentyTwoDaySix: Solution<String, Int>(), Solver {
    const val PART_ONE_SIZE = 4
    const val PART_TWO_SIZE = 14
    override val day: Day
        get() = Day(year = 2022, day = 6)

    override fun parseInput(input: Sequence<String>): Sequence<String> =
        input.map(String::trim).filter(String::isNotBlank)


    override fun partTwo(input: Sequence<String>): Int {
        val string = input.first()
        return string.windowed(size = PART_TWO_SIZE, step = 1).indexOfFirst {
            it.toList().distinct().count() == it.length
        } + PART_TWO_SIZE
    }

    override fun partOne(input: Sequence<String>): Int {
        val string = input.first()
        return string.windowed(size = PART_ONE_SIZE, step = 1).indexOfFirst {
            it.toList().distinct().count() == it.length
        } + PART_ONE_SIZE
    }
}
