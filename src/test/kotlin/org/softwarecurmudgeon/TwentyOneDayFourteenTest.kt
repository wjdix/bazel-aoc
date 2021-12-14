package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyOneDayFourteenTest {
    private val sampleInput = """
        NNCB

        CH -> B
        HH -> N
        CB -> H
        NH -> C
        HB -> C
        HC -> B
        HN -> C
        NN -> C
        BH -> H
        NC -> B
        NB -> B
        BN -> B
        BB -> N
        BC -> B
        CC -> N
        CN -> C
    """.trimIndent().lineSequence()

    @Test
    fun testStep() {
        assertEquals(
            mapOf(
                "NC" to 1L,
                "CN" to 1L,
                "NB" to 1L,
                "BC" to 1L,
                "CH" to 1L,
                "HB" to 1L
            ),
            sampleInput
                .let(TwentyOneDayFourteen::parseInput)
                .let{ TwentyOneDayFourteen.step(it.first(), 1).string }
        )
    }

    @Test
    fun testPartOne() {
        assertEquals(
            1588,
            sampleInput
                .let(TwentyOneDayFourteen::parseInput)
                .let(TwentyOneDayFourteen::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            2188189693529L,
            sampleInput
                .let(TwentyOneDayFourteen::parseInput)
                .let(TwentyOneDayFourteen::partTwo)
        )
    }
}
