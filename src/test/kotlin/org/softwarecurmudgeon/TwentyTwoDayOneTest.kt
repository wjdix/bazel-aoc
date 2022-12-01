package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TwentyTwoDayOneTest {
    private val sampleInput = """
    1000
    2000
    3000

    4000

    5000
    6000

    7000
    8000
    9000

    10000
    """.trimIndent().lineSequence()

    @Test
    fun testPartOne() {
        assertEquals(
            24000,
            sampleInput
                .let(TwentyTwoDayOne::parseInput)
                .let(TwentyTwoDayOne::partOne)

        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            45000,
            sampleInput
                .let(TwentyTwoDayOne::parseInput)
                .let(TwentyTwoDayOne::partTwo)

        )
    }
}
