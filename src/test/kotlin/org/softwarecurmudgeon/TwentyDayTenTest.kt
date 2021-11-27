package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyDayTenTest {
    private val smallSampleInput = """
        16
        10
        15
        5
        1
        11
        7
        19
        6
        12
        4
    """.trimIndent().lineSequence()
    private val sampleInput = """
       28
       33
       18
       42
       31
       14
       46
       20
       48
       47
       24
       23
       49
       45
       19
       38
       39
       11
       1
       32
       25
       35
       8
       17
       7
       9
       4
       2
       34
       10
       3
    """.trimIndent().lineSequence()

    @Test
    fun testSmallPartOne() {
        assertEquals(
            35,
            TwentyDayTen.parseInput(smallSampleInput).let(TwentyDayTen::partOne)
        )
    }
    @Test
    fun testPartOne() {
        assertEquals(
            220,
            TwentyDayTen.parseInput(sampleInput).let(TwentyDayTen::partOne)
        )

    }

}