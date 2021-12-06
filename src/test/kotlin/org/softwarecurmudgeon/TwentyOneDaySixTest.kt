package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyOneDaySixTest {
    private val sampleInput = """
        3,4,3,1,2
    """.trimIndent().lineSequence()
    @Test
    fun testPartOne() {
        assertEquals(
            5934,
            sampleInput
                .let(TwentyOneDaySix::parseInput)
                .let(TwentyOneDaySix::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            26984457539,
            sampleInput
                .let(TwentyOneDaySix::parseInput)
                .let(TwentyOneDaySix::partTwo)
        )
    }
}
