package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TwentyTwoDayThreeTest {
    private val sampleInput = """
        vJrwpWtwJgWrhcsFMMfFFhFp
        jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
        PmmdzqPrVvPwwTWBwg
        wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
        ttgJtRGJQctTZtZT
        CrZsJsPPZsGzwwsLwLmpwMDw
    """.trimIndent().lineSequence()
    @Test
    fun testPartOne() {
        assertEquals(
            157,
            sampleInput
                .let(TwentyTwoDayThree::parseInput)
                .let(TwentyTwoDayThree::partOne)

        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            70,
            sampleInput
                .let(TwentyTwoDayThree::parseInput)
                .let(TwentyTwoDayThree::partTwo)

        )
    }
}
