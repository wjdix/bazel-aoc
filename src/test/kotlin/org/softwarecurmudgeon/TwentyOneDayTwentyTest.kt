package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyOneDayTwentyTest {
    private val sampleAlgorithm = """
        ..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##
        #..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###
        .######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#.
        .#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#.....
        .#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#..
        ...####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.....
        ..##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#
        """
        .trim().lines().map(String::trim).joinToString("")


    private val sampleImage = """
        #..#.
        #....
        ##..#
        ..#..
        ..###
    """.trimIndent()

    private val sampleInput = listOf(sampleAlgorithm, "", sampleImage).joinToString("\n").lineSequence()

    @Test
    fun testParseInput() {
        val parsed = TwentyOneDayTwenty.parseInput(sampleInput)
        assertEquals(
            5,
            parsed.first().image.count()
        )
        assertEquals(
            512,
            parsed.first().enhancements.count()
        )
    }

    @Test
    fun testPartOne() {
        assertEquals(
            35,
            sampleInput
                .let(TwentyOneDayTwenty::parseInput)
                .let(TwentyOneDayTwenty::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            3351,
            sampleInput
                .let(TwentyOneDayTwenty::parseInput)
                .let(TwentyOneDayTwenty::partTwo)
        )
    }
}
