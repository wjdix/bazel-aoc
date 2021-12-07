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
            distanceCost((it - target).absoluteValue)
        }

    private tailrec fun distanceCost(distance: Int, sum: Int = 0): Int =
        if (distance == 0) {
            sum
        } else {
            distanceCost(distance - 1, sum + distance)
        }

    override fun partOne(input: Sequence<Int>): Int =
        input.toList().minOf { target ->
            cost(input.toList(), target)
        }

    override fun partTwo(input: Sequence<Int>): Int =
        0.until(input.maxOrNull()!! + 10).minOf{ target ->
            costTwo(input.toList(), target)
        }
}
