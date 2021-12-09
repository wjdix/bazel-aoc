package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

data class DigitLine(val input: List<String>, val output: List<String>) {
    companion object {
        fun parse(string: String): DigitLine {
            val (input, output) = string
                .split("|")
                .map(String::trim)
                .map { it.split(" ")}
            return DigitLine(input = input, output = output)
        }
    }
}

object TwentyOneDayEight: Solution<DigitLine, Int>(), Solver {
    override val day: Day
        get() = Day(2021, 8)

    override fun parseInput(input: Sequence<String>): Sequence<DigitLine> =
        input
            .filter(String::isNotEmpty)
            .map(DigitLine.Companion::parse)


    override fun partOne(input: Sequence<DigitLine>): Int =
        input.flatMap(DigitLine::output)
            .count { it.length in listOf(2, 4, 3, 7) }

    override fun partTwo(input: Sequence<DigitLine>): Int {
        return input
            .map { line ->
                val translation = findTranslation(line)
                line.output
                    .joinToString("") { translate(translation, it).toString() }
                    .let { println(it); it}
                    .toInt()
            }
            .let { println(it.toList()) ; it}
            .sum()
    }

    private fun findTranslation(input: DigitLine): Map<Set<Char>, Int> {
        val words = input.input + input.output
        val one = words.first { it.length == 2 }
        val four = words.first { it.length == 4 }
        val seven = words.first { it.length == 3 }
        val eight = words.first { it.length == 7 }

        val a: Char = (seven.toSet() - one.toSet()).first()
        val d = (four.toSet() - seven.toSet().minus(a)).first()
        val zero = eight.toSet().minus(d)
        return mapOf(
            zero to 0,
            one.toSet() to 1,
            four.toSet() to 4,
            seven.toSet() to 7,
            eight.toSet() to 8,
        )
    }

    private fun translate(translation: Map<Set<Char>, Int>, it: String): Int {
        return translation[it.toSet()]!!
    }
}
