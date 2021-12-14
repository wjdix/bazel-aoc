package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.BlankLineSeparatedSequence
import org.softwarecurmudgeon.common.Day
import org.softwarecurmudgeon.common.update
import org.softwarecurmudgeon.common.charCounts

typealias Locus = List<Char>
typealias Rule = Pair<Locus, Char>

data class Polymer(
    val string: Map<Locus, Long>,
    val rules: List<Rule>
)

object TwentyOneDayFourteen : Solution<Polymer, Long>(), Solver {
    override val day: Day
        get() = Day(2021, 14)

    override fun parseInput(input: Sequence<String>): Sequence<Polymer> {
        val (string, rules) = BlankLineSeparatedSequence
            .generate(input)
            .toList()

        val parsedRules = rules
            .filter(String::isNotEmpty)
            .map(String::trim)
            .map{ it.split(" -> ") }
            .map{ (first, second) -> Rule(first.toList(), second.first()) }
            .toList()

        return sequenceOf(
            Polymer(
                string = string
                    .first()
                    .windowed(2)
                    .map(String::toList)
                    .associateWith { 1L },
                rules = parsedRules
            )
        )
    }

    private fun solve(input: Polymer, steps: Int): Long {
        val folded = step(input, steps)
        val counts = folded.string.charCounts()
        val max = counts.values.maxOrNull()!!
        val min = counts.values.minOrNull()!!
        return (max - min) / 2 + 1
    }

    override fun partOne(input: Sequence<Polymer>): Long =
        solve(input.first(), 10)

    override fun partTwo(input: Sequence<Polymer>): Long =
        solve(input.first(), 40)

    tailrec fun step(polymer: Polymer, step: Int): Polymer {
        return if (step == 0) {
            polymer
        } else {
            val newCounts = polymer
                .string
                .toList()
                .fold(mapOf<Locus, Long>()) { results, (pair, count) ->
                    val rule = polymer.rules.first { rule -> rule.first == pair }
                    val newPairs = listOf(
                        listOf(
                            pair[0],
                            rule.second
                        ),
                        listOf(
                            rule.second,
                            pair[1]
                        )
                    )
                    newPairs.fold(results) { acc, newPair ->
                        acc.update(newPair, 0) { it + count }
                    }
                }
            step(polymer.copy(string = newCounts), step - 1)
        }
    }
}

