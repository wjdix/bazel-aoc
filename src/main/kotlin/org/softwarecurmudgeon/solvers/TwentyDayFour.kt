package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

data class Passport(
    val birthYear: String?,
    val issueYear: String?,
    val expYear: String?,
    val height: String?,
    val hairColor: String?,
    val eyeColor: String?,
    val passportId: String?,
    val countryId: String?,
) {
    fun valid(): Boolean =
        listOf(
            birthYear,
            issueYear,
            expYear,
            height,
            hairColor,
            eyeColor,
            passportId,
        ).none { it.isNullOrEmpty() }

    companion object {
        fun parse(input: List<String>): Passport =
            input
                .joinToString(" ")
                .split(" ")
                .filter(String::isNotEmpty)
                .associate {
                    val split = it.split(":")
                    Pair(split[0], split[1])
                }
                .let(::buildFromMap)

        private fun buildFromMap(map: Map<String, String>): Passport =
            Passport(
                birthYear = map["byr"],
                issueYear = map["iyr"],
                expYear = map["eyr"],
                height = map["hgt"],
                hairColor = map["hcl"],
                eyeColor = map["ecl"],
                passportId = map["pid"],
                countryId = map["cid"]
            )
    }
}

object TwentyDayFour: Solution<Passport, Int>(), Solver {
    override val day: Day = Day(2020, 4)

    override fun parseInput(input: Sequence<String>): Sequence<Passport> =
        generateSequence(
            seedFunction = {
                val first = input.takeWhile { it.isNotEmpty() }.toList()
                val rest = input.minus(first).drop(1)
                Pair(first, rest)
            },
            nextFunction = { (_, rest) ->
                if (rest.none()) {
                    null
                } else {
                    val first = rest.takeWhile { it.isNotEmpty() }.toList()
                    val newRest = rest.minus(first).drop(1)
                    Pair(first, newRest)
                }
            }
        )
            .map { it.first }
            .map(Passport.Companion::parse)

    override fun partOne(input: Sequence<Passport>): Int {
        val (valid, _) = input.partition(Passport::valid)
        valid.forEach(::println)
        return valid.count()
    }

    override fun partTwo(input: Sequence<Passport>): Int = 4
}
