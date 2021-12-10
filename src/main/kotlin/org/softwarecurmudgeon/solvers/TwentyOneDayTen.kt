package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

object TwentyOneDayTen: Solution<List<Char>, Long>(), Solver {
    override val day: Day
        get() = Day(2021, 10)

    override fun parseInput(input: Sequence<String>): Sequence<List<Char>> =
        input
            .filter(String::isNotEmpty)
            .map(String::toList)

    override fun partOne(input: Sequence<List<Char>>): Long =
        input
            .filter { corrupt(it) }
            .map { firstCorrupt(it) }
            .map {
                when(it) {
                    ')' -> 3L
                    ']' -> 57L
                    '}' -> 1197L
                    '>' -> 25137L
                    else -> 0L
                }
            }
            .sum()

    override fun partTwo(input: Sequence<List<Char>>): Long {
        val scores = input
            .map(::findCompletion)
            .filter(List<Char>::isNotEmpty)
            .map(::scoreCompletion)
            .sortedDescending()
            .toList()
        return scores[scores.count() / 2]
    }

    private val openers = setOf(
        '[',
        '(',
        '{',
        '<'
    )

    private fun closerForOpener(opener: Char) =
        when(opener) {
            '[' -> ']'
            '(' -> ')'
            '{' -> '}'
            '<' -> '>'
            else -> throw IllegalArgumentException("bad opener")
        }

    fun corrupt(line: List<Char>): Boolean =
        !validate(line)

    fun scoreCompletion(input: List<Char>): Long =
        input.fold(0) { score, next ->
            (score * 5) + when(next) {
                ')' -> 1
                ']' -> 2
                '}' -> 3
                '>' -> 4
                else -> throw IllegalArgumentException("Bad closer")
            }
        }

    fun firstCorrupt(remaining: List<Char>): Char? =
        navigateParens(
            remaining,
            listOf(),
            null,
            { null },
            { it }
        )

    fun findCompletion(remaining: List<Char>, searching: List<Char> = listOf()): List<Char> =
        navigateParens(
            remaining,
            searching,
            listOf(),
            { it.reversed() },
            { listOf() }
        )

    private fun validate(remaining: List<Char>) =
        navigateParens(
            remaining,
            listOf(),
            true,
            { false },
            { false }
        )

    private tailrec fun <T> navigateParens(
        remaining: List<Char>,
        searching: List<Char> = listOf(),
        emptyCase: T,
        emptyRemainingCase: (List<Char>) -> T,
        mismatchCase: (Char) -> T
    ): T = if (remaining.isEmpty() && searching.isEmpty()) {
        emptyCase
    }  else if (remaining.isEmpty() && searching.isNotEmpty())
        emptyRemainingCase(searching)
    else {
        val first = remaining.first()
        val rest = remaining.drop(1)
        when (first) {
            in openers -> navigateParens(
                rest,
                searching.plus(closerForOpener(first)),
                emptyCase,
                emptyRemainingCase,
                mismatchCase
            )
            searching.last() -> navigateParens(
                rest,
                searching.dropLast(1),
                emptyCase,
                emptyRemainingCase,
                mismatchCase
            )
            else -> mismatchCase(first)
        }
    }

}
