package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.BlankLineSeparatedSequence
import org.softwarecurmudgeon.common.Day

typealias Rule = Pair<String, Char>

data class Polymer(
    val string: Map<String, Long>,
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
            .map{ (first, second) -> Rule(first, second.first()) }
            .toList()

        return sequenceOf(
            Polymer(
                string = string
                    .first()
                    .windowed(2)
                    .associateWith { 1L },
                rules = parsedRules
            )
        )
    }

    override fun partOne(input: Sequence<Polymer>): Long {
        val folded = step(input.first(), 10)
        val counts = folded.string.charCounts()
        val max = counts.values.maxOrNull()!!
        val min = counts.values.minOrNull()!!
        return (max - min) / 2 + 1
    }

    override fun partTwo(input: Sequence<Polymer>): Long {
        val folded = step(input.first(), 40)
        val counts = folded.string.charCounts()
        val max = counts.values.maxOrNull()!!
        val min = counts.values.minOrNull()!!
        return (max - min) / 2 + 1
    }

    tailrec fun step(polymer: Polymer, step: Int): Polymer {
        return if (step == 0) {
            polymer
        } else {
            val newPairs = polymer
                .string.flatMap { (pair, count) ->
                    val rule = polymer.rules.first { rule -> rule.first == pair }
                    listOf(
                        Pair(
                            pair.take(1).plus(rule.second),
                            count
                        ),
                        Pair(
                            rule.second.toString().plus(pair.drop(1)),
                            count
                        )
                    )
                }
                .merge()
            step(polymer.copy(string = newPairs), step - 1)
        }
    }
}

private fun Map<String, Long>.charCounts(): Map<Char, Long> =
    this.toList().fold(mutableMapOf()) { acc, (pair: String, count: Long) ->
        pair.forEach { char ->
            if (acc[char] != null) {
                acc[char] = acc[char]!! + count
            } else {
                acc[char] = count
            }
        }
        acc
    }


private fun List<Pair<String, Long>>.merge(): Map<String, Long> =
    this.fold(mutableMapOf()) { acc, (pair, count) ->
        if (acc[pair] != null) {
            acc[pair] = acc[pair]!! + count
        } else {
            acc[pair] = count
        }
        acc
    }
