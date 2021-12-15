package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyOneDayFifteenTest {
    private val sampleInput = """
        1163751742
        1381373672
        2136511328
        3694931569
        7463417111
        1319128137
        1359912421
        3125421639
        1293138521
        2311944581
    """.trimIndent().lineSequence()

    @Test
    fun testPartOne() {
        assertEquals(
            40,
            sampleInput
                .let(TwentyOneDayFifteen::parseInput)
                .let(TwentyOneDayFifteen::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            315,
            sampleInput
                .let(TwentyOneDayFifteen::parseInput)
                .let(TwentyOneDayFifteen::partTwo)
        )
    }
}
