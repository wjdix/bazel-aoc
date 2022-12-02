package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TwentyTwoDayTwoTest {
    private val sampleInput = """
        A Y
        B X
        C Z
    """.trimIndent().lineSequence()
    @Test
    fun testPartOne() {
        assertEquals(
            15,
            sampleInput
                .let(TwentyTwoDayTwo::parseInput)
                .let(TwentyTwoDayTwo::partOne)

        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            12,
            sampleInput
                .let(TwentyTwoDayTwo::parseInput)
                .let(TwentyTwoDayTwo::partTwo)

        )
    }
}
