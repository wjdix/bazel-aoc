package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyDayTwelveTest {
    private val sampleInput = """
        F10
        N3
        F7
        R90
        F11
    """.trimIndent().lineSequence()
    @Test
    fun testPartOne() {
        assertEquals(
            25,
            TwentyDayTwelve.parseInput(sampleInput).let(TwentyDayTwelve::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            286,
            TwentyDayTwelve.parseInput(sampleInput).let(TwentyDayTwelve::partTwo)
        )
    }
}
