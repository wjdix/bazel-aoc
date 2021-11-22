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

    fun valid2(): Boolean =
        birthYear?.toIntOrNull() in (1920..2002)
                && issueYear?.toIntOrNull() in (2010..2020)
                && expYear?.toIntOrNull() in (2020..2030)
                && validHeight(height)
                && validHairColor(hairColor)
                && validEyeColor(eyeColor)
                && validPassportId(passportId)

    private fun validPassportId(passportId: String?): Boolean =
        if (passportId.isNullOrEmpty()) {
            false
        } else {
            "\\d{9}".toRegex().matchEntire(passportId)?.let { true } ?: false
        }

    private fun validEyeColor(eyeColor: String?): Boolean =
        if (eyeColor.isNullOrEmpty()) {
            false
        } else {
            eyeColor in setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
        }

    private fun validHairColor(hairColor: String?): Boolean =
        if (hairColor.isNullOrEmpty()) {
            false
        } else {
            "#[a-f0-9]{6}".toRegex()
                .matchEntire(hairColor)
                ?.let { true }
                ?: false
        }

    private fun validHeight(height: String?): Boolean =
        if (height.isNullOrEmpty()) {
            false
        }  else {
            "(\\d+)(cm|in)"
                .toRegex()
                .matchEntire(height)
                ?.groupValues
                ?.let { (_, digits, scale) ->
                    when (scale) {
                        "cm" -> {
                            digits.toIntOrNull() in (150..193)
                        }
                        "in" -> {
                            digits.toIntOrNull() in (59..76)
                        }
                        else -> false
                    }

                }
                ?: false
        }


    companion object {
        fun parse(input: Sequence<String>): Passport =
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
                val first = input.takeWhile { it.isNotEmpty() }
                val rest = input.drop(first.count() + 1)
                Pair(first, rest)
            },
            nextFunction = { (_, rest) ->
                if (rest.none()) {
                    null
                } else {
                    val first = rest.takeWhile { it.isNotEmpty() }
                    val newRest = rest.drop(first.count() + 1)
                    Pair(first, newRest)
                }
            }
        )
            .map { it.first }
            .map(Passport.Companion::parse)

    override fun partOne(input: Sequence<Passport>): Int =
        input.count(Passport::valid)

    override fun partTwo(input: Sequence<Passport>): Int =
        input.count(Passport::valid2)
}
