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
                    .toInt()
            }
            .sum()
    }

    private fun findTranslation(input: DigitLine): Map<Set<Char>, Int> {
        val words = (input.input + input.output)
            .map(String::toSet)
            .distinct()
        val one = words.first { it.count() == 2 }
        val four = words.first { it.count() == 4 }
        val seven = words.first { it.count() == 3 }
        val eight = words.first { it.count() == 7 }
        val nine = words
            .filter { it.count() == 6 }
            .first { nineCandidate ->
                four.all { it in nineCandidate }
            }
        val zero = words
            .filter { it.count() == 6 }
            .minus(listOf(nine))
            .first { zeroCandidate ->
                one.all { it in zeroCandidate }
            }
        val six = words
            .filter { it.count() == 6}
            .minus(listOf(zero, nine))
            .first()
        val five = words
            .filter { it.count() == 5}
            .first {
                six.intersect(it) == it
            }
        val three = words
            .filter { it.count() == 5 }
            .first { threeCandidate ->
                threeCandidate.intersect(one) == one
            }
        val two = words.first { twoCandidate ->
            twoCandidate.count() == 5 && twoCandidate !in listOf(five, three)
        }

    return mapOf(
            zero to 0,
            one to 1,
            two to 2,
            three to 3,
            four to 4,
            five to 5,
            six to 6,
            seven to 7,
            eight to 8,
            nine to 9,
        )
    }

    private fun translate(translation: Map<Set<Char>, Int>, it: String): Int {
        return translation[it.toSet()]!!
    }
}
