package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyOneDayTwelveTest {
    private val sampleInput = """
        start-A
        start-b
        A-c
        A-b
        b-d
        A-end
        b-end
    """.trimIndent().lineSequence()

    private val mediumInput = """
        dc-end
        HN-start
        start-kj
        dc-start
        dc-HN
        LN-dc
        HN-end
        kj-sa
        kj-HN
        kj-dc
    """.trimIndent().lineSequence()

    private val largerSampleInput = """
       fs-end
       he-DX
       fs-he
       start-DX
       pj-DX
       end-zg
       zg-sl
       zg-pj
       pj-he
       RW-he
       fs-DX
       pj-RW
       zg-RW
       start-pj
       he-WI
       zg-he
       pj-fs
       start-RW
    """.trimIndent().lineSequence()

    @Test
    fun testPartOne() {
        assertEquals(
            10,
            sampleInput
                .let(TwentyOneDayTwelve::parseInput)
                .let(TwentyOneDayTwelve::partOne)
        )
    }

    @Test
    fun testMediumPartOne() {
        assertEquals(
            19,
            mediumInput
                .let(TwentyOneDayTwelve::parseInput)
                .let(TwentyOneDayTwelve::partOne)
        )
    }

    @Test
    fun testLongerPartOne() {
        assertEquals(
            226,
            largerSampleInput
                .let(TwentyOneDayTwelve::parseInput)
                .let(TwentyOneDayTwelve::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            36,
            sampleInput
                .let(TwentyOneDayTwelve::parseInput)
                .let(TwentyOneDayTwelve::partTwo)
        )
    }
}
