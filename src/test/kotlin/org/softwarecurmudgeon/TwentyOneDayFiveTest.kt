package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyOneDayFiveTest {
    private val sampleInput = """
        199
        200
        208
        210
        200
        207
        240
        269
        260
        263
    """.trimIndent().lineSequence()
    @Test
    fun testPartOne() {
        assertEquals(
            7,
            TwentyOneDayFive.parseInput(sampleInput).let(TwentyOneDayFive::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            5,
            TwentyOneDayFive.parseInput(sampleInput).let(TwentyOneDayFive::partTwo)
        )
    }
}
