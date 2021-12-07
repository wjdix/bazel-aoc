package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyOneDaySevenTest {
    private val sampleInput = """
        16,1,2,0,4,2,7,1,2,14
    """.trimIndent().lineSequence()
    @Test
    fun testPartOne() {
        assertEquals(
            37,
            sampleInput
                .let(TwentyOneDaySeven::parseInput)
                .let(TwentyOneDaySeven::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            168,
            sampleInput
                .let(TwentyOneDaySeven::parseInput)
                .let(TwentyOneDaySeven::partTwo)
        )
    }
}
