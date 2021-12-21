package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyOneDayTwentyOneTest {
    @Test
    fun testPartOne() {
        assertEquals(
            739785,
            sequenceOf(
                Game(
                    player1Position = 4,
                    player2Position = 8
                )
            )
                .let(TwentyOneDayTwentyOne::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            444356092776315,
            sequenceOf(
                Game(
                    player1Position = 4,
                    player2Position = 8
                )
            )
                .let(TwentyOneDayTwentyOne::partTwo)
        )

    }
}
