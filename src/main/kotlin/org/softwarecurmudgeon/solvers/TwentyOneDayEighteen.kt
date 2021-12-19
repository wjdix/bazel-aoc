package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day
import java.text.ParseException

object SnailfishNumberParser {
    private val digitPattern = Regex("\\A(\\d+)(.*)")
    private val leftStart = Regex("\\A\\[(.*)")
    private val rightStart = Regex("\\A,(.*)")
    private val rightEnd = Regex("\\A](.*)")

    tailrec fun tokenize(input: String, acc: List<SnailfishNumber.Token> = emptyList()): List<SnailfishNumber.Token> =
        if (input.isEmpty()) {
            acc
        } else {
            when {
                digitPattern.matches(input) -> {
                    digitPattern.matchEntire(input)!!.groupValues.let { (_, digits, rest) ->
                        return tokenize(rest, acc.plus(SnailfishNumber.Token.Literal(digits.toInt())))
                    }
                }
                leftStart.matches(input) -> {
                    leftStart.matchEntire(input)!!.groupValues.let { (_, rest) ->
                        return tokenize(rest, acc.plus(SnailfishNumber.Token.LeftStart))
                    }
                }
                rightStart.matches(input) -> {
                    rightStart.matchEntire(input)!!.groupValues.let { (_, rest) ->
                        return tokenize(rest, acc.plus(SnailfishNumber.Token.RightStart))
                    }
                }
                rightEnd.matches(input) -> {
                    rightEnd.matchEntire(input)!!.groupValues.let { (_, rest) ->
                        return tokenize(rest, acc.plus(SnailfishNumber.Token.RightEnd))
                    }
                }
                else -> throw ParseException("Can't tokenize", input.count())
            }
        }

    private fun parse(input: List<SnailfishNumber.Token>): SnailfishNumber =
        innerParse(input).let { (parsed, rest) ->
            if (rest.isNotEmpty()) {
                throw ParseException("Unparsed tokens: $rest", rest.count())
            } else {
                parsed
            }
        }

    private fun innerParse(input: List<SnailfishNumber.Token>): Pair<SnailfishNumber, List<SnailfishNumber.Token>> =
        when(val first = input.first()) {
            is SnailfishNumber.Token.Literal -> Pair(SnailfishNumber.Literal(first.number), input.drop(1))
            SnailfishNumber.Token.LeftStart -> parsePair(input.drop(1))
            else -> throw ParseException("Parsing state exception: $input", input.count())
        }

    private fun parsePair(
        input: List<SnailfishNumber.Token>,
        left: SnailfishNumber? = null,
        right: SnailfishNumber? = null,
    ): Pair<SnailfishNumber, List<SnailfishNumber.Token>> =
        if (left != null && right != null) {
            Pair(SnailfishNumber.SPair(left = left, right = right), input)
        } else if (left != null && right == null) {
            val (newRight, rest) = parseRight(input)
            Pair(SnailfishNumber.SPair(left = left, right = newRight), rest)
        } else {
            val (newLeft, rest) = parseLeft(input)
            parsePair(rest, left = newLeft)
        }

    private fun parseLeft(input: List<SnailfishNumber.Token>): Pair<SnailfishNumber, List<SnailfishNumber.Token>> {
        val (parsed, rest) = innerParse(input)
        if (rest.first() != SnailfishNumber.Token.RightStart) {
            throw ParseException("Parsing state exception: $input, $rest)", input.count())
        }
        return Pair(parsed, rest.drop(1))
    }

    private fun parseRight(input: List<SnailfishNumber.Token>) : Pair<SnailfishNumber, List<SnailfishNumber.Token>> {
        val (right, rest) = innerParse(input)
        if (rest.first() != SnailfishNumber.Token.RightEnd) {
            throw ParseException("Parsing state exception: $input, $rest)", input.count())
        }

        return Pair(right, rest.drop(1))
    }
    fun parse(input: String): SnailfishNumber =
        input
            .let(::tokenize)
            .let(::parse)
}

sealed interface SnailfishNumber {
    fun magnitude() : Int
    data class Literal(val number: Int) : SnailfishNumber {
        override fun magnitude(): Int = number
        override fun toString(): String =
            number.toString()
    }

    data class SPair(val left: SnailfishNumber, val right: SnailfishNumber) : SnailfishNumber {
        override fun magnitude(): Int =
            (3 * left.magnitude()) + (2 * right.magnitude())

        override fun toString(): String =
            "[$left,$right]"
    }

    fun plus(other: SnailfishNumber): SnailfishNumber =
        SPair(left = this, right = other)

    sealed interface Token {
        data class Literal(val number: Int) : Token
        object LeftStart : Token
        object RightStart : Token
        object RightEnd : Token
    }

    companion object {
        data class ExplosionState(
            val number: SnailfishNumber,
            val depth: Int = 0,
            val exploded: Boolean = false,
            val leftRemainder: Int? = null,
            val rightRemainder: Int? = null
        )

        private fun handleExplosionLeft(number: SPair, leftState: ExplosionState): ExplosionState {
            val (newRight, rightRemainder) = addRight(number.right, leftState.rightRemainder)
            return ExplosionState(
                exploded = true,
                number = SPair(
                    left = leftState.number,
                    right = newRight,
                ),
                rightRemainder = rightRemainder,
                leftRemainder = leftState.leftRemainder
            )
        }

        @Suppress("NestedBlockDepth")
        fun explode(state: ExplosionState): ExplosionState =
            when (state.number) {
                is Literal -> state
                is SPair -> {
                    if (state.depth < 4) {
                        val leftState = explode(
                            ExplosionState(
                                depth = state.depth + 1,
                                exploded = false,
                                number = state.number.left,
                            )
                        )
                        if (leftState.exploded) {
                            handleExplosionLeft(state.number, leftState)
                        } else {
                            val rightState = explode(
                                ExplosionState(
                                    depth = state.depth +1,
                                    exploded = false,
                                    number = state.number.right
                                )
                            )
                            if (rightState.exploded) {
                                handleExplosionRight(state.number, rightState)
                            } else {
                                state
                            }
                        }
                    } else {
                        ExplosionState(
                            number = Literal(0),
                            exploded = true,
                            leftRemainder = (state.number.left as Literal).number,
                            rightRemainder = (state.number.right as Literal).number,
                            depth = state.depth
                        )
                    }
            }
        }

        private fun handleExplosionRight(
            number: SPair,
            rightState: ExplosionState
        ): ExplosionState {
            val (newLeft, leftRemainder) = addLeft(number.left, rightState.leftRemainder)
            return ExplosionState(
                exploded = true,
                number = SPair(
                    left = newLeft,
                    right = rightState.number
                ),
                rightRemainder = rightState.rightRemainder,
                leftRemainder = leftRemainder,
            )
        }

        fun explode(number:SnailfishNumber): Pair<SnailfishNumber, Boolean> =
            explode(ExplosionState(number = number)).let {state ->
                Pair(state.number, state.exploded)
            }

        private fun reduceLiteral(number: SnailfishNumber): Pair<SnailfishNumber, Boolean> =
            when (number) {
                is Literal -> {
                    if (number.number > 9) {
                        val left = number.number / 2
                        val right = number.number - left
                        Pair(
                            SPair(
                                left = Literal(left),
                                right = Literal(right)
                            ),
                            true
                        )
                    } else {
                        Pair(
                            number,
                            false
                        )
                    }
                }
                is SPair -> {
                    val (left, reduced) = reduceLiteral(number.left)
                    if (reduced) {
                        Pair(
                            SPair(
                                left = left,
                                right = number.right
                            ),
                            true
                        )
                    } else {
                        val (right, rightReduced) = reduceLiteral(number.right)
                        Pair(
                            SPair(
                                left = number.left,
                                right = right,
                            ),
                            rightReduced
                        )
                    }
                }
            }

        fun reduce(number: SnailfishNumber): SnailfishNumber =
            generateSequence(Triple(number, true, true)) {
                val (exploded, didExplode) = explode(it.first)
                if (didExplode) {
                    Triple(exploded, didExplode, false)
                } else {
                    val (reduced, didReduce) = reduceLiteral(exploded)
                    Triple(reduced, false, didReduce)
                }
            }
                .first { (_, didExplode, didReduce) ->
                    !didExplode && !didReduce
                }.first


        fun addRight(number: SnailfishNumber, i: Int?): Pair<SnailfishNumber, Int?> =
            if (i == null) {
                Pair(number, null)
            } else {
                when (number) {
                    is Literal -> Pair(Literal(number.number + i), null)
                    is SPair -> {
                        val (left, leftRemainder) = addRight(number.left, i)
                        val (right, rightRemainder) = addRight(number.right, leftRemainder)
                        Pair(
                            SPair(left = left, right = right), rightRemainder
                        )
                    }
                }
            }

        fun addLeft(number: SnailfishNumber, i: Int?): Pair<SnailfishNumber, Int?> =
            if (i == null) {
                Pair(number, i)
            } else {
                when (number) {
                    is Literal -> Pair(Literal(number.number + i), null)
                    is SPair -> {
                        val (right, rightRemainder) = addLeft(number.right, i)
                        val (left, leftRemainder) = addLeft(number.left, rightRemainder)
                        Pair(
                            SPair(left = left, right = right),
                            leftRemainder
                        )
                    }
                }
            }
    }
}

object TwentyOneDayEighteen: Solution<SnailfishNumber, Int>(), Solver {
    override val day: Day
        get() = Day(2021, 18)

    override fun parseInput(input: Sequence<String>): Sequence<SnailfishNumber> =
        input
            .filter(String::isNotEmpty)
            .map(SnailfishNumberParser::parse)

    override fun partOne(input: Sequence<SnailfishNumber>): Int =
        input.reduce{ first, second -> SnailfishNumber.reduce(first.plus(second)) }
            .magnitude()

    override fun partTwo(input: Sequence<SnailfishNumber>): Int =
        input.flatMap { x ->
            input.minus(x).flatMap { y ->
                listOf(
                    SnailfishNumber.reduce(x.plus(y)),
                    SnailfishNumber.reduce(y.plus(x))
                )
            }
        }
            .maxOf(SnailfishNumber::magnitude)
}
