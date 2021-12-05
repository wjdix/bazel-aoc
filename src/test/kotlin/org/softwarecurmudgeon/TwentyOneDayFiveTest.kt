package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class TwentyOneDayFiveTest {
    private val sampleInput = """
        0,9 -> 5,9
        8,0 -> 0,8
        9,4 -> 3,4
        2,2 -> 2,1
        7,0 -> 7,4
        6,4 -> 2,0
        0,9 -> 2,9
        3,4 -> 1,4
        0,0 -> 8,8
        5,5 -> 8,2
    """.trimIndent().lineSequence()
    @Test
    fun testPartOne() {
        assertEquals(
            5,
            sampleInput
                .let(TwentyOneDayFive::parseInput)
                .let(TwentyOneDayFive::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            12,
            sampleInput
                .let(TwentyOneDayFive::parseInput)
                .let(TwentyOneDayFive::partTwo)
        )
    }

    @Test
    fun testDiagonalCoords() {
        assertEquals(
            listOf(
                Coords(1, 1),
                Coords(2, 2),
                Coords(3, 3)
            ),
            Pair(Coords(1, 1), Coords(3, 3)).diagonalCoords()
        )
        assertEquals(
            listOf(
                Coords(9, 7),
                Coords(8, 8),
                Coords(7, 9)
            ),

            Pair(Coords(9,  7), Coords(7, 9)).diagonalCoords()
        )
    }
}
