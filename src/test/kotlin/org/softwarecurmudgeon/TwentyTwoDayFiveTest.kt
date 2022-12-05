package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TwentyTwoDayFiveTest {
    private val sampleInput = """
    [D]    
[N] [C]    
[Z] [M] [P]
 1   2   3 

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2
    """.trimIndent().lineSequence()

    @Test
    fun testPartOne() {
        assertEquals(
            "CMZ",
            sampleInput
                .let(TwentyTwoDayFive::parseInput)
                .let(TwentyTwoDayFive::partOne)

        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            "MCD",
            sampleInput
                .let(TwentyTwoDayFive::parseInput)
                .let(TwentyTwoDayFive::partTwo)

        )
    }
}
