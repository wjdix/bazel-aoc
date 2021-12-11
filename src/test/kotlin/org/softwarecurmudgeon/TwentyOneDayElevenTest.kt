package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyOneDayElevenTest {
    private val sampleInput = """
        5483143223
        2745854711
        5264556173
        6141336146
        6357385478
        4167524645
        2176841721
        6882881134
        4846848554
        5283751526
    """.trimIndent().lineSequence()

    @Test
    fun testPartOne() {
        assertEquals(
            204,
            sampleInput
                .let(TwentyOneDayEleven::parseInput)
                .let { TwentyOneDayEleven.flashesAfterSteps(10, it) }
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            195,
            sampleInput.let(TwentyOneDayEleven::parseInput).let(TwentyOneDayEleven::partTwo)
        )
    }
}
