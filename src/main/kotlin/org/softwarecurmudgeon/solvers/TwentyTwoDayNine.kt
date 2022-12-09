package org.softwarecurmudgeon.solvers

import kotlin.math.absoluteValue
import kotlin.math.sign
import org.softwarecurmudgeon.common.Day

enum class RopeDirection {
    U, D, L, R
}

typealias RopeInstruction = Pair<RopeDirection, Int>
data class RopeState(
    val knotPositions: List<Pair<Int, Int>> = listOf(Pair(0, 0), Pair(0,0)),
    val tailVisited: Set<Pair<Int, Int>> = setOf(Pair(0,0))
)

object TwentyTwoDayNine: Solution<RopeInstruction, Int>(), Solver {
    override val day: Day
        get() = Day(year = 2022, day = 9)

    override fun parseInput(input: Sequence<String>): Sequence<RopeInstruction> =
        input.filter(String::isNotEmpty).map(::parseLine)

    private fun parseLine(line: String): RopeInstruction {
        val (direction, length) = line.split(" ")
        return RopeInstruction(
            RopeDirection.valueOf(direction),
            length.toInt()
        )
    }

    override fun partOne(input: Sequence<RopeInstruction>): Int {
        return input.fold(RopeState()) { state, (direction, length) ->
            ropeStep(length, state, direction)
        }.tailVisited.count()
    }
    

    override fun partTwo(input: Sequence<RopeInstruction>): Int {
        return input.fold(RopeState(
            knotPositions = 0.until(10).map { Pair(0,0) }.toList()
        )) { state, (direction, length) -> ropeStep(length, state, direction)
        }.tailVisited.count()
    }

    private fun ropeStep(
        length: Int,
        state: RopeState,
        direction: RopeDirection
    ) = 0.until(length).fold(state) { lastState, _ ->
        val nextHead = lastState.knotPositions.first().move(direction)
        val nextPositions = lastState.knotPositions.drop(1).runningFold(nextHead) { head, tail ->
            head.tailPosition(tail)
        }
        lastState.copy(
            knotPositions = nextPositions,
            tailVisited = lastState.tailVisited.plus(nextPositions.last())
        )
    }

}

private fun  Pair<Int, Int>.tailPosition(tailPosition: Pair<Int, Int>): Pair<Int, Int> {
    val diff = Pair(this.first - tailPosition.first, this.second - tailPosition.second)
    return when (diff) {
        Pair(0, 0) -> tailPosition
        Pair(0, 1) -> tailPosition
        Pair(0, -1) -> tailPosition
        Pair(1, 0) -> tailPosition
        Pair(-1, 0) -> tailPosition
        Pair(-1, 1) -> tailPosition
        Pair(1, -1) -> tailPosition
        Pair(1, 1) -> tailPosition
        Pair(-1, -1) -> tailPosition
        else -> {
            val newX = when (diff.first.sign) {
                1 -> tailPosition.first + 1
                0 -> tailPosition.first
                -1 -> tailPosition.first - 1
                else -> throw IllegalArgumentException("${diff.first.sign} ")
            }
            val newY = when (diff.second.sign) {
                1 -> tailPosition.second + 1
                0 -> tailPosition.second
                -1 -> tailPosition.second - 1
                else -> throw IllegalArgumentException("${diff.second.sign} ")
            }
            Pair(newX, newY)
        }
    }
}

private fun Pair<Int, Int>.move(direction: RopeDirection): Pair<Int, Int> =
    when(direction) {
        RopeDirection.U -> Pair(this.first, this.second + 1)
        RopeDirection.D -> Pair(this.first, this.second - 1)
        RopeDirection.L -> Pair(this.first - 1, this.second)
        RopeDirection.R -> Pair(this.first + 1, this.second)
    }
