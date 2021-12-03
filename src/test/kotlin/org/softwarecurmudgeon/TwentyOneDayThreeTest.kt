package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyOneDayThreeTest {
    private val sampleInput = """
        00100
        11110
        10110
        10111
        10101
        01111
        00111
        11100
        10000
        11001
        00010
        01010
    """.trimIndent().lineSequence()

    @Test
    fun testPartOne() {
        assertEquals(
            198,
            sampleInput
                .let(TwentyOneDayThree::parseInput)
                .let(TwentyOneDayThree::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            230,
            sampleInput
                .let(TwentyOneDayThree::parseInput)
                .let(TwentyOneDayThree::partTwo)
        )
    }
}
