package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyOneDayThirteenTest {
    private val sampleInput = """
        6,10
        0,14
        9,10
        0,3
        10,4
        4,11
        6,0
        6,12
        4,1
        0,13
        10,12
        3,4
        3,0
        8,4
        1,10
        2,14
        8,10
        9,0

        fold along y=7
        fold along x=5
    """.trimIndent().lineSequence()



    @Test
    fun testPartOne() {
        assertEquals(
           17L,
            sampleInput
                .let(TwentyOneDayThirteen::parseInput)
                .let(TwentyOneDayThirteen::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            4L,
            sampleInput
                .let(TwentyOneDayThirteen::parseInput)
                .let(TwentyOneDayThirteen::partTwo)
        )
    }
}
