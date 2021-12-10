package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyOneDayTenTest {
    private val sampleInput = """
        [({(<(())[]>[[{[]{<()<>>
        [(()[<>])]({[<{<<[]>>(
        {([(<{}[<>[]}>{[]{[(<()>
        (((({<>}<{<{<>}{[]{[]{}
        [[<[([]))<([[{}[[()]]]
        [{[{({}]{}}([{[{{{}}([]
        {<[[]]>}<{[{[{[]{()[[[]
        [<(<(<(<{}))><([]([]()
        <{([([[(<>()){}]>(<<{{
        <{([{{}}[<[[[<>{}]]]>[]]
    """.trimIndent().lineSequence()

    @Test
    fun testCorrupt() {
        assert(
            TwentyOneDayTen.corrupt("(]".toList())
        )
        assert(
            TwentyOneDayTen.corrupt("{()()()>)".toList())
        )
        assert(
            TwentyOneDayTen.corrupt("(((()))}".toList())
        )
    }

    @Test
    fun testFirstCorrupt() {
        assertEquals(
        ')',
            "[[<[([]))<([[{}[[()]]]".toList().let(TwentyOneDayTen::firstCorrupt)
        )
        assertEquals(
            ']',
            "[{[{({}]{}}([{[{{{}}([]".toList().let(TwentyOneDayTen::firstCorrupt)
        )
    }

    @Test
    fun testFindCompletion() {
        assertEquals(
            "}}]])})]".toList(),
            "[({(<(())[]>[[{[]{<()<>>".toList().let(TwentyOneDayTen::findCompletion)
        )
        assertEquals(
            ")}>]})".toList(),
            "[(()[<>])]({[<{<<[]>>(".toList().let(TwentyOneDayTen::findCompletion)
        )
    }

    @Test
    fun testScoreCompletion(){
        assertEquals(
            294,
            "])}>".toList().let(TwentyOneDayTen::scoreCompletion)
        )
    }

    @Test
    fun testPartOne() {
        assertEquals(
            26397,
            sampleInput
                .let(TwentyOneDayTen::parseInput)
                .let(TwentyOneDayTen::partOne)
        )
    }
    @Test
    fun testPartTwo() {
        assertEquals(
            288957,
            sampleInput
                .let(TwentyOneDayTen::parseInput)
                .let(TwentyOneDayTen::partTwo)
        )
    }
}
