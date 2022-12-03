package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

object TwentyTwoDayThree: Solution<Pair<Set<Char>, Set<Char>>, Int>(), Solver {
    override val day: Day
        get() = Day(year = 2022, day = 3)

    override fun parseInput(input: Sequence<String>): Sequence<Pair<Set<Char>, Set<Char>>> {
        return input
            .filter(String::isNotEmpty)
            .map {
                val length = it.length
                val first = it.take(length / 2)
                val second = it.drop(length / 2)

                Pair(first.toSet(), second.toSet())
            }
    }

    override fun partTwo(input: Sequence<Pair<Set<Char>, Set<Char>>>): Int {
        return input.windowed(size = 3, step = 3).map { (first, second, third) ->
            val firstSet = first.toList().flatten().toSet()
            val secondSet = second.toList().flatten().toSet()
            val thirdSet = third.toList().flatten().toSet()
            firstSet.intersect(secondSet).intersect(thirdSet).first().priority()
        }.sum()
    }

    override fun partOne(input: Sequence<Pair<Set<Char>, Set<Char>>>): Int {
        return input.map { (first, second) ->
            first.intersect(second).first().priority()
        }.sum()
    }
}

val PRIORITY_RANGE = ('a'..'z') + ('A'..'Z')

private fun Char.priority(): Int {
    return PRIORITY_RANGE.indexOf(this) + 1
}
