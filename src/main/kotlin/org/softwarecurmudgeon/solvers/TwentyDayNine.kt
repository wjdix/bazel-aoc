package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

data class RingBuffer(
    val size: Int,
    val elements: List<Int> = emptyList(),
) {
    fun add(element: Int) =
        this.copy(
            elements = elements.plus(element).takeLast(size)
        )

    private val valids: List<Int>
        get() = elements.flatMap { outer ->
            elements.minus(outer).map { inner -> inner + outer}
        }

    fun isValid(element: Int) = elements.count() < size || element in valids
}

object TwentyDayNine: Solution<Int, Int>(), Solver {
    override val day: Day
        get() = Day(2020, 9)

    override fun parseInput(input: Sequence<String>): Sequence<Int> =
        input.filter(String::isNotEmpty).mapNotNull(String::toIntOrNull)

    override fun partOne(input: Sequence<Int>): Int = solvePartOneFor(25, input)!!

    override fun partTwo(input: Sequence<Int>): Int =
        2.until(100_000).asSequence().flatMap { size ->
            input.windowed(size)
        }.first {
            it.sum() == 90433990
        }.let {
            val sorted = it.sorted()
            sorted.first() + sorted.last()
        }

    fun solvePartOneFor(bufferSize: Int, input: Sequence<Int>) =
        input
            .runningFold(
                Triple<Int?, Boolean, RingBuffer>(null, true, RingBuffer(bufferSize))
            ) { (_, _, buffer), it ->
                val newBuffer = buffer.add(it)
                Triple(it, buffer.isValid(it), newBuffer)
            }
            .first { !it.second }
            .first
}
