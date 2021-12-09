package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day
import kotlin.math.absoluteValue

object TwentyOneDaySeven: Solution<Int, Int>(), Solver {
    override val day: Day
        get() = Day(2021, 7)

    override fun parseInput(input: Sequence<String>): Sequence<Int> =
        input
            .filter(String::isNotEmpty)
            .joinToString("")
            .split(",")
            .mapNotNull(String::toIntOrNull)
            .asSequence()


    private fun cost(elements: List<Int>, target:Int): Int =
        elements.sumOf {
            (it - target).absoluteValue
        }

    private fun costTwo(elements: List<Int>, target: Int) =
        elements.sumOf {
            triangle((it - target).absoluteValue)
        }

    private fun triangle(n: Int): Int = (n * (n + 1)) / 2

    override fun partOne(input: Sequence<Int>): Int =
        input.toList().minOf { target ->
            cost(input.toList(), target)
        }

    override fun partTwo(input: Sequence<Int>): Int =
        0.until(input.maxOrNull()!!).minOf{ target ->
            costTwo(input.toList(), target)
        }
}
