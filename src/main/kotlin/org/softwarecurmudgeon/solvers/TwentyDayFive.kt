package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

data class BinarySeat(val bytes: String) {
    val seatId: Int
        get() = bytes.toInt(2)
    val row: Int
        get() = bytes.dropLast(3).toInt(2)
    val column: Int
        get() = bytes.drop(7).toInt(2)
    companion object {
        fun parse(input: String): BinarySeat =
            BinarySeat(
            input
                .replace('F', '0')
                .replace('B', '1')
                .replace('L', '0')
                .replace('R', '1')
            )
    }
}

object TwentyDayFive: Solution<BinarySeat, Int>(), Solver {
    override val day: Day = Day(2020, 5)

    override fun parseInput(input: Sequence<String>): Sequence<BinarySeat> =
        input
            .filter(String::isNotEmpty)
            .map(BinarySeat.Companion::parse)

    override fun partOne(input: Sequence<BinarySeat>): Int =
        input.maxOf(BinarySeat::seatId)

    override fun partTwo(input: Sequence<BinarySeat>): Int {
        val present = input.map(BinarySeat::seatId).toSet()
        val candidates = (1..965).toSet().minus(present)
        return candidates.first { candidate ->
            candidate + 1 !in candidates
                    && candidate - 1 !in candidates
        }
    }

}
