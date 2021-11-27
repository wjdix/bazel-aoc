package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

data class ChargerChain(
    val chained: List<Int> = listOf(0),
    val remaining: List<Int>,
) {
    fun completed(): Boolean = remaining.isEmpty()
    fun candidates(): List<ChargerChain> {
        val head = chained.lastOrNull() ?: 0
        val candidates = remaining.filter { it in (head + 1)..(head + 3)}
        return candidates.map {
            this.copy(
                chained = chained.plus(it),
                remaining = remaining.minus(it)
            )
        }
    }
    fun compute() =
        chained
            .windowed(2)
            .fold(Pair(0, 1)) { acc, (x, y) ->
                when ((y - x)) {
                    1 -> {
                        val newAcc = acc.copy(
                            first = acc.first + 1
                        )
                        newAcc
                    }
                    3 -> {
                        val newAcc = acc.copy(
                            second = acc.second + 1
                        )
                        newAcc
                    }
                    else ->  acc
                }
            }
            .let { (x, y) -> x * y}
}

data class ChargerSolver(
    val current: ChargerChain,
    val next: List<ChargerChain> = emptyList(),
) {
    fun next() : ChargerSolver = current.candidates().let { candidates ->
        val newNext = (next + candidates).sortedBy { it.remaining.count() }
        this.copy(
            current = newNext.first(),
            next = newNext.drop(1)
        )
    }
}

object TwentyDayTen: Solution<Int, Int>(), Solver {
    override val day: Day
        get() = Day(2020, 10)

    override fun parseInput(input: Sequence<String>): Sequence<Int> =
        input
            .filter(String::isNotEmpty)
            .mapNotNull(String::toIntOrNull)
            .sorted()

    override fun partOne(input: Sequence<Int>): Int =
        generateSequence(
            seed = ChargerSolver(
                current = ChargerChain(remaining = input.toList())
            ),
            nextFunction = { it.next() }
        )
            .map(ChargerSolver::current)
            .firstOrNull(ChargerChain::completed)
            ?.compute()
            ?: -1

    override fun partTwo(input: Sequence<Int>): Int {
        val max = input.maxOrNull()
        val min = input.minOrNull() ?: 0
        return 4
    }
}
