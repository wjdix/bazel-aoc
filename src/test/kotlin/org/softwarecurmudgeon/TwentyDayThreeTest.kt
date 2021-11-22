package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class TwentyDayThreeTest {
    private val input = """
        ..##.......
        #...#...#..
        .#....#..#.
        ..#.#...#.#
        .#...##..#.
        ..#.##.....
        .#.#.#....#
        .#........#
        #.##...#...
        #...##....#
        .#..#...#.#
    """.trimIndent().lineSequence()
    @Test
    fun testPartOne() {
        assertEquals(
            7,
            TwentyDayThree.parseInput(input).let(TwentyDayThree::partOne)
        )
    }

    @Test
    fun testTraverse() {
        assertEquals(
            2,
            TwentyDayThree
                .parseInput(input)
                .let{ TwentyDayThree.traverse(it, 1, 1) }
        )
    }

    @Test
    fun testTraverseAgain() {
        assertEquals(
            3,
            TwentyDayThree
                .parseInput(input)
                .let{ TwentyDayThree.traverse(it, 5, 1) }
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            336,
            TwentyDayThree.parseInput(input).let(TwentyDayThree::partTwo)
        )
    }
}
