package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyOneDaySeventeenTest {
    private val sampleInput = """
        target area: x=20..30, y=-10..-5
    """.trimIndent().lineSequence()

    @Test
    fun testPartOne() {
        assertEquals(
            45,
            sampleInput
                .let(TwentyOneDaySeventeen::parseInput)
                .let(TwentyOneDaySeventeen::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            112,
            sampleInput
                .let(TwentyOneDaySeventeen::parseInput)
                .let(TwentyOneDaySeventeen::partTwo)
        )
    }
}
