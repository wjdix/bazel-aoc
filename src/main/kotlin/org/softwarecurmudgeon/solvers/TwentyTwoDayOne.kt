package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

object TwentyTwoDayOne : Solution<Int?, Int>(), Solver {
    override val day: Day
        get() = Day(year = 2022, day = 1)

    override fun parseInput(input: Sequence<String>): Sequence<Int?> {
        return input.map {
            it.toIntOrNull()
        }
    }

    override fun partTwo(input: Sequence<Int?>): Int {
        return input.fold(Pair(emptyList<Int>(), 0)) { (sums, current), element ->
            if (element == null) {
                Pair(sums + current, 0)
            } else {
                Pair(sums, current + element)
            }
        }.let { (sums, current) ->
            sums.plus(current).sortedDescending().take(n = 3).sum()
        }
    }

    override fun partOne(input: Sequence<Int?>): Int {
        return input.fold(Pair(0, 0)) {(max, current), element ->
            if (element == null) {
                Pair(max.coerceAtLeast(current), 0)
            } else {
                Pair(max, current + element)
            }
        }.first
    }
}
