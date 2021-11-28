package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyDayElevenTest {
    private val sampleInput = """
        L.LL.LL.LL
        LLLLLLL.LL
        L.L.L..L..
        LLLL.LL.LL
        L.LL.LL.LL
        L.LLLLL.LL
        ..L.L.....
        LLLLLLLLLL
        L.LLLLLL.L
        L.LLLLL.LL
    """.trimIndent().lineSequence()

    @Test
    fun testPartOne() {
        assertEquals(
            37,
            TwentyDayEleven.parseInput(sampleInput).let(TwentyDayEleven::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
           26,
            TwentyDayEleven.parseInput(sampleInput).let(TwentyDayEleven::partTwo)
        )
    }
}
