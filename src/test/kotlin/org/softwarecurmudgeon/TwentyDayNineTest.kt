package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyDayNineTest {
    private val sampleInput = """
        35
        20
        15
        25
        47
        40
        62
        55
        65
        95
        102
        117
        150
        182
        127
        219
        299
        277
        309
        576 
    """.trimIndent().lineSequence()
    @Test
    fun testPartOne() {
        assertEquals(
            127,
            TwentyDayNine.parseInput(sampleInput).let { TwentyDayNine.solvePartOneFor(5, it) }
        )
    }
}
