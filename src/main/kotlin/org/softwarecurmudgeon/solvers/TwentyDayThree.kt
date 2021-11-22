package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

data class Position(val x: Int = 0, val y: Int = 0)

data class State(val position: Position = Position(), val trees: Int = 0) {
    fun descend(x: Int, y: Int) = this.copy(
        position = this.position.copy(
            x = this.position.x + x,
            y = this.position.y + y
        )
    )
}

object TwentyDayThree: Solution<Sequence<Char>, Int>(), Solver {
    override val day: Day = Day(2020, 3)

    override fun parseInput(input: Sequence<String>): Sequence<Sequence<Char>> =
        input.map { line ->
            generateSequence(line.toList()) { it }.flatten()
        }

    fun traverse(input: Sequence<Sequence<Char>>, x: Int, y: Int) =
        1.until(input.count()).fold(State()) { state, _ ->
            val line = input.elementAt(state.position.y)
            val descended = if (line.elementAt(state.position.x) == '#') {
                state.descend(x, y).let { descended ->
                    descended.copy(trees = descended.trees + 1)
                }
            } else {
                state.descend(x, y)
            }
            if (descended.position.y >= input.count()) {
                return descended.trees
            } else {
                descended
            }
        }
            .trees

    override fun partOne(input: Sequence<Sequence<Char>>): Int =
        traverse(input, 3, 1)

    override fun partTwo(input: Sequence<Sequence<Char>>): Int =
        listOf(
            Pair(1, 1),
            Pair(3, 1),
            Pair(5, 1),
            Pair(7, 1),
            Pair(1, 2)
        ).fold(1) { acc, (x, y) ->
            acc * traverse(input, x, y)
        }
}
