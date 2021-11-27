package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

data class ChargerChain(
    val chained: List<Long> = listOf(0),
    val remaining: List<Long>,
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
    fun compute() : Long =
        chained
            .windowed(2)
            .fold(Pair(0L, 1L)) { acc, (x, y) ->
                when ((y - x)) {
                    1L -> {
                        val newAcc = acc.copy(
                            first = acc.first + 1
                        )
                        newAcc
                    }
                    3L -> {
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
class Paths {
    val memo: MutableMap<Pair<Long, Long>, Long> =
        mutableMapOf(
            Pair(0L, 0L) to 1L
        )

    fun set(from: Long, to: Long, pathCount: Long): Paths {
        memo[Pair(from, to)] = pathCount
        return this
    }

    fun get(from: Long, to:Long): Long = memo[Pair(from, to)] ?: 0L
}

object TwentyDayTen: Solution<Long, Long>(), Solver {
    override val day: Day
        get() = Day(2020, 10)

    override fun parseInput(input: Sequence<String>): Sequence<Long> =
        input
            .filter(String::isNotEmpty)
            .mapNotNull(String::toLongOrNull)
            .sorted()

    override fun partOne(input: Sequence<Long>): Long =
        generateSequence(
            seed = ChargerSolver(
                current = ChargerChain(remaining = input.toList())
            ),
            nextFunction = { it.next() }
        )
            .map(ChargerSolver::current)
            .firstOrNull(ChargerChain::completed)
            ?.compute()
            ?: -1L

    override fun partTwo(input: Sequence<Long>): Long =
        input.maxOrNull()?.let { max ->
            input
                .sorted()
                .fold(Paths()) { paths, int ->
                    paths.set(
                        from = 0,
                        to = int,
                        pathCount = (int - 3).until(int).sumOf { paths.get(0, it) }
                    )
                }
                .get(0, max)
        }
            ?: -1L
}
