package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyOneDayEighteenTest {
    private val sampleInput = """
        [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
        [[[5,[2,8]],4],[5,[[9,9],0]]]
        [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
        [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
        [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
        [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
        [[[[5,4],[7,7]],8],[[8,3],8]]
        [[9,3],[[9,9],[6,[4,9]]]]
        [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
        [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
    """.trimIndent().lineSequence()

    @Test
    fun testTokenize() {
        assertEquals(
            listOf(SnailfishNumber.Token.Literal(9)),
            SnailfishNumberParser.tokenize("9")
        )
        assertEquals(
            listOf(
                SnailfishNumber.Token.LeftStart,
                SnailfishNumber.Token.Literal(9),
                SnailfishNumber.Token.RightStart,
                SnailfishNumber.Token.Literal(5),
                SnailfishNumber.Token.RightEnd
            ),
            SnailfishNumberParser.tokenize("[9,5]")
        )
    }
    @Test
    fun testParse() {
        assertEquals(
            SnailfishNumber.Literal(1),
            SnailfishNumberParser.parse("1")
        )
        assertEquals(
            SnailfishNumber.SPair(
                left = SnailfishNumber.Literal(1),
                right = SnailfishNumber.Literal(2)
            ),
            SnailfishNumberParser.parse("[1,2]")
        )
        assertEquals(
            SnailfishNumber.SPair(
                left = SnailfishNumber.SPair(
                    left = SnailfishNumber.Literal(1),
                    right = SnailfishNumber.Literal(2)
                ),
                right = SnailfishNumber.Literal(3)
            ),
            SnailfishNumberParser.parse("[[1,2],3]")
        )
    }

    @Test
    fun testPlus() {
        assertEquals(
            SnailfishNumber.SPair(
                left = SnailfishNumber.Literal(1),
                right = SnailfishNumber.Literal(2)
            ),
            SnailfishNumber.Literal(1).plus(SnailfishNumber.Literal(2))
        )
    }

    @Test
    fun testExplode() {
        assertEquals(
            Pair("[[[[0,9],2],3],4]".let(SnailfishNumberParser::parse), true),
            "[[[[[9,8],1],2],3],4]"
                .let(SnailfishNumberParser::parse)
                .let(SnailfishNumber.Companion::explode)
        )
    }

    @Test
    fun testExplodeAgain() {
        assertEquals(
            Pair("[7,[6,[5,[7,0]]]]".let(SnailfishNumberParser::parse), true),
            "[7,[6,[5,[4,[3,2]]]]]"
                .let(SnailfishNumberParser::parse)
                .let(SnailfishNumber.Companion::explode)
        )
    }

    @Test
    fun testExplodeThrice() {
        assertEquals(
            Pair("[[6,[5,[7,0]]],3]".let(SnailfishNumberParser::parse), true),
            "[[6,[5,[4,[3,2]]]],1]"
                .let(SnailfishNumberParser::parse)
                .let(SnailfishNumber.Companion::explode)
        )
    }

    @Test
    fun testExplodeFourth() {
        assertEquals(
            Pair("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]".let(SnailfishNumberParser::parse), true),
            "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"
                .let(SnailfishNumberParser::parse)
                .let(SnailfishNumber.Companion::explode)
        )
    }

    @Test
    fun testAddRight() {
        assertEquals(
            Pair("[[1,1],2]".let(SnailfishNumberParser::parse), null),
            "[[0,1],2]".let(SnailfishNumberParser::parse).let {
                SnailfishNumber.addRight(it, 1)
            }
        )
    }

    @Test
    fun testAddLeft() {
        assertEquals(
            Pair("[0,[1,3]]".let(SnailfishNumberParser::parse), null),
            "[0,[1,2]]".let(SnailfishNumberParser::parse).let {
                SnailfishNumber.addLeft(it, 1)
            }
        )
    }

    @Test
    fun testReduce() {
        assertEquals(
            "[[[[0,9],2],3],4]".let(SnailfishNumberParser::parse),
            "[[[[[9,8],1],2],3],4]"
                .let(SnailfishNumberParser::parse)
                .let(SnailfishNumber.Companion::reduce)
        )
    }

    @Test
    fun testMagnitude() {
        assertEquals(
            143,
            "[[1,2],[[3,4],5]]".let(SnailfishNumberParser::parse).magnitude()
        )

        assertEquals(
            1384,
            "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]".let(SnailfishNumberParser::parse).magnitude()
        )
    }

    @Test
    fun testWithoutMagnitude() {
        assertEquals(
            "[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]".let(SnailfishNumberParser::parse),
            sampleInput
                .let(TwentyOneDayEighteen::parseInput)
                .reduce { first, other -> SnailfishNumber.reduce(first.plus(other)) }
        )
    }

    @Test
    fun testPartOne() {
        assertEquals(
            4140,
            sampleInput
                .let(TwentyOneDayEighteen::parseInput)
                .let(TwentyOneDayEighteen::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        assertEquals(
            3993,
            sampleInput
                .let(TwentyOneDayEighteen::parseInput)
                .let(TwentyOneDayEighteen::partTwo)
        )
    }
}
