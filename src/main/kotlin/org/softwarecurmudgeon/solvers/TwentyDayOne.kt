package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

object TwentyDayOne: Solution<Int, Int>(), Solver {
    override val day = Day(2020, 1)
    override fun partOne(input: Sequence<Int>): Int =
        input.flatMap { outer ->
            input.map { inner ->
                Pair(outer, inner)
            }
        }
            .first { (x, y) -> x + y == 2020 }
            .let {(x, y) -> x * y }

    override fun partTwo(input: Sequence<Int>): Int =
        input.flatMap { outer ->
            input.flatMap { inner ->
                input.map { inner2 ->
                    Triple(outer, inner, inner2)
                }
            }
        }
            .first { (x, y, z) -> x + y + z == 2020}
            .let { (x, y, z) -> x * y * z }

    override fun parseInput(input: Sequence<String>): Sequence<Int> =
        input.mapNotNull { it.toIntOrNull() }
}