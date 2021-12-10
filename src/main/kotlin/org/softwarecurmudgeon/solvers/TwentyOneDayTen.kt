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

    tailrec fun firstCorrupt(remaining: List<Char>, searching: List<Char> = listOf()): Char? =
        if (remaining.isEmpty() && searching.isEmpty()) {
            null
        }  else if (remaining.isEmpty() && searching.isNotEmpty())
            null
        else {
            val first = remaining.first()
            val rest = remaining.drop(1)
            when (first) {
                in openers -> firstCorrupt(rest, searching.plus(closerForOpener(first)))
                searching.last() -> firstCorrupt(rest, searching.dropLast(1))
                else -> first
            }
        }

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

    tailrec fun findCompletion(remaining: List<Char>, searching: List<Char> = listOf()): List<Char> =
        if (remaining.isEmpty() && searching.isEmpty()) {
            listOf()
        }  else if (remaining.isEmpty() && searching.isNotEmpty())
            searching.reversed()
        else {
            val first = remaining.first()
            val rest = remaining.drop(1)
            when (first) {
                in openers -> findCompletion(rest, searching.plus(closerForOpener(first)))
                searching.last() -> findCompletion(rest, searching.dropLast(1))
                else -> listOf()
            }
        }

    private tailrec fun validate(remaining: List<Char>, searching: List<Char> = listOf()): Boolean =
        if (remaining.isEmpty() && searching.isEmpty()) {
            true
        }  else if (remaining.isEmpty() && searching.isNotEmpty())
            false
        else {
            val first = remaining.first()
            val rest = remaining.drop(1)
            when (first) {
                in openers -> validate(rest, searching.plus(first))
                searching.first() -> validate(rest, searching.drop(1))
                else -> false
            }
        }

    override fun partTwo(input: Sequence<List<Char>>): Long {
        val scores = input
            .map(::findCompletion)
            .filter(List<Char>::isNotEmpty)
            .map(::scoreCompletion)
            .sortedDescending()
            .toList()
        println(scores.count())
        println( scores[(scores.count() / 2) + 1])
        println( scores[(scores.count() / 2) - 1])
        println( scores[(scores.count() / 2)])
        return scores[scores.count() / 2]
    }
}
