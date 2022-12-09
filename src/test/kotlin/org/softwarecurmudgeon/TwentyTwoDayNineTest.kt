package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class TwentyTwoDayNineTest {
    private val sampleInput = """
        R 4
        U 4
        L 3
        D 1
        R 4
        D 1
        L 5
        R 2
    """.trimIndent().lineSequence()
    private val largeSampleInput = """
        R 5
        U 8
        L 8
        D 3
        R 17
        D 10
        L 25
        U 20
    """.trimIndent().lineSequence()

    @Test
    fun testPartOne() {
        assertEquals(
            13,
            sampleInput
                .let(TwentyTwoDayNine::parseInput)
                .let(TwentyTwoDayNine::partOne)
        )
    }
    @Test
    fun testPartTwo() {
        assertEquals(
            1,
            sampleInput
                .let(TwentyTwoDayNine::parseInput)
                .let(TwentyTwoDayNine::partTwo)
        )
    }
    @Test
    fun testPartTwoLarge() {
        assertEquals(
            36,
            largeSampleInput
                .let(TwentyTwoDayNine::parseInput)
                .let(TwentyTwoDayNine::partTwo)
        )
    }
}
