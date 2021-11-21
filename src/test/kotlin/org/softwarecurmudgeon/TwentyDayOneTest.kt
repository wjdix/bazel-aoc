package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TwentyDayOneTest {
    private val sampleInput =
        sequenceOf(
            1721,
            979,
            366,
            299,
            675,
            1456,
        )
    @Test
    fun testPartOne() {
        assertEquals(
            514579,
            TwentyDayOne.partOne(sampleInput)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            241861950,
            TwentyDayOne.partTwo(sampleInput)
        )
    }
}
