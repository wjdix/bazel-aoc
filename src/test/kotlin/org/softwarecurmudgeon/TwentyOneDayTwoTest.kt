package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyOneDayTwoTest {
    private val sampleInput = """
        forward 5
        down 5
        forward 8
        up 3
        down 8
        forward 2
    """.trimIndent().lineSequence()

    @Test
    fun testPartOne() {
        assertEquals(
            150,
            TwentyOneDayTwo.parseInput(sampleInput).let(TwentyOneDayTwo::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            900,
            TwentyOneDayTwo.parseInput(sampleInput).let(TwentyOneDayTwo::partTwo)
        )
    }
}
