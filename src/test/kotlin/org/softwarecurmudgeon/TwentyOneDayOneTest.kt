package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyOneDayOneTest {
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
            TwentyOneDayOne.parseInput(sampleInput).let(TwentyOneDayOne::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            5,
            TwentyOneDayOne.parseInput(sampleInput).let(TwentyOneDayOne::partTwo)
        )
    }
}
