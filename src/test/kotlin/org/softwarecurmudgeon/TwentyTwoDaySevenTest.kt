package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class TwentyTwoDaySevenTest {
    private val sampleInput = """
        ${'$'} cd /
        ${'$'} ls
        dir a
        14848514 b.txt
        8504156 c.dat
        dir d
        ${'$'} cd a
        ${'$'} ls
        dir e
        29116 f
        2557 g
        62596 h.lst
        ${'$'} cd e
        ${'$'} ls
        584 i
        ${'$'} cd ..
        ${'$'} cd ..
        ${'$'} cd d
        ${'$'} ls
        4060174 j
        8033020 d.log
        5626152 d.ext
        7214296 k
    """.trimIndent().lineSequence()

    @Test
    fun testPartOne() {
        assertEquals(
            95437,
            sampleInput
                .let(TwentyTwoDaySeven::parseInput)
                .let(TwentyTwoDaySeven::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            24933642,
            sampleInput
                .let(TwentyTwoDaySeven::parseInput)
                .let(TwentyTwoDaySeven::partTwo)
        )
    }
}
