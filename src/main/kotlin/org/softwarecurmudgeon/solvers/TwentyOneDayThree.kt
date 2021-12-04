package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

object TwentyOneDayThree: Solution<String, Int>(), Solver {
    override val day: Day
        get() = Day(2021, 3)

    override fun parseInput(input: Sequence<String>): Sequence<String>  =
        input
            .filter(String::isNotEmpty)

    override fun partOne(input: Sequence<String>): Int {
        val gamma = 0.until(input.first().length).map { position ->
            input
                .map { it[position] }
                .groupBy { it }
                .maxByOrNull { (_, b) -> b.count() }
                ?.key!!
        }
        val epsilon = gamma.map {
            when (it) {
                '1' -> '0'
                '0' -> '1'
                else -> '4'
            }
        }
        return gamma.joinToString("").toInt(2) * epsilon.joinToString("").toInt(2)
    }

    private fun mostCommonAtPositionOrTies(input: List<String>, position: Int): Char {
        val foo = input
            .map { it[position] }
            .groupBy { it }
        return if (foo['1']?.count()!! >= foo['0']?.count()!!) {
            '1'
        } else {
            '0'
        }
    }

    private fun leastCommonAtPositionOrTies(input: List<String>, position: Int): Char {
        val foo = input
            .map { it[position] }
            .groupBy { it }
        return if (foo['1']?.count()!! >= foo['0']?.count()!!) {
            '0'
        } else {
            '1'
        }
    }

    private tailrec fun co2Rating(input: List<String>, targetPosition: Int = 0): String {
        return if (input.count() == 1) {
            input.first()
        } else {
            val common = leastCommonAtPositionOrTies(input, targetPosition)
            val newInput = input.filter { it[targetPosition] == common }
            co2Rating(newInput, targetPosition + 1)
        }
    }

    private tailrec fun oxygenRating(input: List<String>, targetPosition: Int = 0): String {
        return if (input.count() == 1) {
            input.first()
        } else {
            val common = mostCommonAtPositionOrTies(input, targetPosition)
            val newInput = input.filter { it[targetPosition] == common }
            oxygenRating(newInput, targetPosition + 1)
        }
    }

    override fun partTwo(input: Sequence<String>): Int =
        oxygenRating(input.toList()).toInt(2) *
                co2Rating(input.toList()).toInt(2)
}
