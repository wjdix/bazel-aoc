package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.BlankLineSeparatedSequence
import org.softwarecurmudgeon.common.Day

typealias Board = List<List<Int>>

data class Bingo(
    val calls: List<Int> = emptyList(),
    val boards: List<Board> = emptyList(),
    val called: Set<Int> = emptySet(),
    val completedBoards: Set<Board> = emptySet(),
) {
    fun next(): Bingo {
        val newCalled = called.plus(calls.first())
        val newCalls = calls.drop(1)
        val (completed, notCompleted) = boards.partition { isCompleteBoard(newCalled, it) }

        return Bingo(
            calls = newCalls,
            called = newCalled,
            completedBoards = completedBoards.plus(completed),
            boards = notCompleted
        )
    }
    companion object {
        fun isCompleteBoard(called: Set<Int>, board: Board): Boolean =
            board.any { row -> row.all { it in called } }
                    || 0.until(board.first().count()).any { columnIndex ->
                board.map { it[columnIndex] }.all { it in called } }

        fun parse(sequence: List<Sequence<String>>): Sequence<Bingo> {
            val calls = sequence
                .first()
                .first()
                .split(",")
                .mapNotNull(String::toIntOrNull)
            val boards = sequence
                .drop(1)
                .map {
                    it.map { line ->
                        line.split(" ").mapNotNull(String::toIntOrNull)
                    }
                        .toList()
                }
                .toList()
            return sequenceOf(
                Bingo(
                    calls = calls,
                    boards = boards
                )
            )
        }
    }
}

object TwentyOneDayFour: Solution<Bingo, Int>(), Solver {
    override val day: Day
        get() = Day(2021, 4)

    override fun parseInput(input: Sequence<String>): Sequence<Bingo> =
        BlankLineSeparatedSequence.generate(input).toList().let(Bingo::parse)

    override fun partOne(input: Sequence<Bingo>): Int =
        generateSequence(input.first()) { it.next() }
            .first {
                it.completedBoards.count() == 1
            }
            .let {
                calculate(it.completedBoards.first(), it.called, it.called.last())
            }

    private fun calculate(board: Board, calls: Set<Int>, lastCall: Int): Int =
        lastCall * board.flatten().filterNot { it in calls }.sum()


    override fun partTwo(input: Sequence<Bingo>): Int =
        generateSequence(input.first()) { it.next() }
            .first { bingo ->
                bingo.boards.isEmpty()
            }
            .let {
                calculate(it.completedBoards.last(), it.called, it.called.last())
            }
}
