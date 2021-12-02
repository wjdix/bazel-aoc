package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyOneDayThreeTest {
    private val sampleInput = """
        
    """.trimIndent().lineSequence()

    @Test
    fun testPartOne() {
        assertEquals(
            4,
            sampleInput
                .let(TwentyOneDayThree::parseInput)
                .let(TwentyOneDayThree::partOne)
        )
    }
    @Test
    fun testPartTwo() {
        assertEquals(
            5,
            sampleInput
                .let(TwentyOneDayThree::parseInput)
                .let(TwentyOneDayThree::partTwo)
        )
    }
}
