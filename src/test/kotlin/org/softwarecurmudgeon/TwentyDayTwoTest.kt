package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

internal class TwentyDayTwoTest {
    private val input = sequenceOf(
        "1-3 a: abcde",
        "1-3 b: cdefg",
        "2-9 c: ccccccccc",
    )
    @Test
    fun testParseInput() {
        assertEquals(
            listOf(
                Password(min = 1, max = 3, char = 'a', password = "abcde"),
                Password(min = 1, max = 3, char = 'b', password = "cdefg"),
                Password(min = 2, max = 9, char = 'c', password = "ccccccccc"),
            ),
            TwentyDayTwo.parseInput(input).toList()
        )
    }
    @Test
    fun testPartOne() {
        assertEquals(
            2,
            TwentyDayTwo.parseInput(input).let(TwentyDayTwo::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            1,
            TwentyDayTwo.parseInput(input).let(TwentyDayTwo::partTwo)
        )
    }
}