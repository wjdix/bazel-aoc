package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyDayThirteenTest {
    private val sampleInput = """
        939
        7,13,x,x,59,x,31,19
    """.trimIndent().lineSequence()
    @Test
    fun testPartOne() {
        assertEquals(
            295,
            TwentyDayThirteen.parseInput(sampleInput).let(TwentyDayThirteen::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            1068781,
            TwentyDayThirteen.parseInput(sampleInput).let(TwentyDayThirteen::partTwo)
        )
    }
}
