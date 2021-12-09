package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyOneDayNineTest {
    private val sampleInput = """
        2199943210
        3987894921
        9856789892
        8767896789
        9899965678
    """.trimIndent().lineSequence()

    @Test
    fun testPartOne() {
        assertEquals(
            15,
            sampleInput
                .let(TwentyOneDayNine::parseInput)
                .let(TwentyOneDayNine::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            1134,
            sampleInput
                .let(TwentyOneDayNine::parseInput)
                .let(TwentyOneDayNine::partTwo)
        )
    }
}
