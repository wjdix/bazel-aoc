package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TwentyTwoDayEightTest {

    private val sampleInput = """
        30373
        25512
        65332
        33549
        35390
    """.trimIndent().lineSequence()

    @Test
    fun testPartOne() {
        assertEquals(
            21,
            sampleInput
                .let(TwentyTwoDayEight::parseInput)
                .let(TwentyTwoDayEight::partOne)
        )
    }
    @Test
    fun testPartTwo() {
        assertEquals(
            8,
            sampleInput
                .let(TwentyTwoDayEight::parseInput)
                .let(TwentyTwoDayEight::partTwo)
        )
    }
}
