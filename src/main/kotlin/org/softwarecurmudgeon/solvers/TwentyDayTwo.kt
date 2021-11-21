package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

data class Password(
    val min: Int,
    val max: Int,
    val char: Char,
    val password: String
) {
    fun validPartOne(): Boolean =
        password.count { it == char } in min..max

    fun validPartTwo(): Boolean =
        (password.getOrNull(min - 1) == char).xor(
            password.getOrNull(max - 1) == char)
}

object TwentyDayTwo: Solution<Password, Int>(), Solver {
    override val day: Day = Day(2020, 2)

    override fun parseInput(input: Sequence<String>): Sequence<Password> =
        input.mapNotNull(::parseLine)

    private fun parseLine(line: String): Password? =
        "(\\d+)-(\\d+) (\\w): (\\w+)"
            .toRegex()
            .matchEntire(line)
            ?.groupValues
            ?.let { (_, min, max, char, password) ->
                Password(
                    min = min.toInt(),
                    max = max.toInt(),
                    char = char.first(),
                    password = password,
                )
            }

    override fun partOne(input: Sequence<Password>): Int =
        input.count(Password::validPartOne)

    override fun partTwo(input: Sequence<Password>): Int =
        input.count(Password::validPartTwo)
}