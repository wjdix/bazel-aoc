package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TwentyTwoDaySixTest {

    @Test
    fun testPartOne() {
        assertEquals(
            5,
            TwentyTwoDaySix.partOne(sequenceOf("bvwbjplbgvbhsrlpgdmjqwftvncz"))
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            19,
            TwentyTwoDaySix.partTwo(sequenceOf("mjqjpqmgbljsphdztnvjfqwrcgsmlb"))
        )
    }
}
