package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyDaySixTest {
    private val sampleInput = """
        abc

        a
        b
        c

        ab
        ac

        a
        a
        a
        a

        b
    """.trimIndent().lineSequence()
    @Test
    fun testPartOne() {
        assertEquals(
            11,
            TwentyDaySix.parseInput(sampleInput).let(TwentyDaySix::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            6,
            TwentyDaySix.parseInput(sampleInput).let(TwentyDaySix::partTwo)
        )
    }
}
