package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class EighteenDayOneTest {
    private val sampleInput = """
       1
       -2
       +3
       +1
    """.trimIndent().lineSequence()
    @Test
    fun testPartOne() {
        assertEquals(
            3,
            sampleInput
                .let(EighteenDayOne::parseInput)
                .let(EighteenDayOne::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            2,
            sampleInput
                .let(EighteenDayOne::parseInput)
                .let(EighteenDayOne::partTwo)
        )
    }
}
