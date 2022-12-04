package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TwentyTwoDayFourTest {
    private val sampleInput = """
        2-4,6-8
        2-3,4-5
        5-7,7-9
        2-8,3-7
        6-6,4-6
        2-6,4-8
    """.trimIndent().lineSequence()

    @Test
    fun testPartOne() {
        assertEquals(
            2,
            sampleInput
                .let(TwentyTwoDayFour::parseInput)
                .let(TwentyTwoDayFour::partOne)

        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            4,
            sampleInput
                .let(TwentyTwoDayFour::parseInput)
                .let(TwentyTwoDayFour::partTwo)

        )
    }
}
