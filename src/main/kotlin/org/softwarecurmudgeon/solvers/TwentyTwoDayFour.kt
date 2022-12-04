package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

typealias ElfAssignment = Pair<Int, Int>
typealias AssignmentPair = Pair<ElfAssignment, ElfAssignment>

object TwentyTwoDayFour: Solution<AssignmentPair, Int>(), Solver {
    override val day: Day
        get() = Day(year = 2022, day = 4)

    override fun parseInput(input: Sequence<String>): Sequence<AssignmentPair> =
        input
            .filter(String::isNotEmpty)
            .map {
                val (first, second) = it.split(",")
                Pair(parseRange(first), parseRange(second))
            }

    private fun parseRange(first: String): ElfAssignment =
        first.split("-").map(String::toInt).let { (first, second) ->
            Pair(first, second)
        }

    override fun partTwo(input: Sequence<AssignmentPair>): Int =
        input.filter{ (first, second) ->
            val firstRange = (first.first..first.second).toSet()
            val secondRange = (second.first..second.second).toSet()
            firstRange.intersect(secondRange).isNotEmpty()
        }.count()

    override fun partOne(input: Sequence<AssignmentPair>): Int =
        input.count { (first, second) ->
            first.contains(second) || second.contains(first)
        }
}

private fun ElfAssignment.contains(other: ElfAssignment): Boolean =
    this.first <= other.first && this.second >= other.second
