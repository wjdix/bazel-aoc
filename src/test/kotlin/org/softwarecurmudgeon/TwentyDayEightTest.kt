package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyDayEightTest {
    private val sampleInput = """
       nop +0
       acc +1
       jmp +4
       acc +3
       jmp -3
       acc -99
       acc +1
       jmp -4
       acc +6
    """.trimIndent().lineSequence()
    @Test
    fun testPartOne() {
        assertEquals(
            5,
            TwentyDayEight.parseInput(sampleInput).let(TwentyDayEight::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            8,
            TwentyDayEight.parseInput(sampleInput).let(TwentyDayEight::partTwo)
        )
    }
}
