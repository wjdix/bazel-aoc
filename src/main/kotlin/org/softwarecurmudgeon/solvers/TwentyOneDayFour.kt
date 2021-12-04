package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.BlankLineSeparatedSequence
import org.softwarecurmudgeon.common.Day

typealias Board = List<List<Int>>

data class Bingo(val calls: List<Int> = emptyList(), val boards: List<Board> = emptyList()) {
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

    override fun partOne(input: Sequence<Bingo>): Int {
        val bingo = input.first()
        val calls = bingo.calls
        val (_, completeCalls, completedBoard) = calls
            .runningFold(setOf<Int>()) { previous, current ->
                previous.plus(current)
            }
            .flatMap { called ->
                bingo.boards.map { Triple(Bingo.isCompleteBoard(called, it), called, it) }
            }
            .first { (complete, _, _) ->
                complete
            }

        return calculate(completedBoard, completeCalls, completeCalls.last())
    }

    private fun calculate(board: Board, calls: Set<Int>, lastCall: Int): Int =
        lastCall * board.flatten().filterNot { it in calls }.sum()


    override fun partTwo(input: Sequence<Bingo>): Int {
        val bingo = input.first()
        val calls = bingo.calls
        val toComplete = bingo.boards.count()
        var completed = setOf<Board>()
        val (_, completeCalls, completedBoard) = calls
            .runningFold(setOf<Int>()) { previous, current ->
                previous.plus(current)
            }
            .flatMap { called ->
                bingo.boards.map { Triple(Bingo.isCompleteBoard(called, it), called, it) }
            }.filter { (complete, _, _) ->
                complete
            }.first{ (_, _, board) ->
                completed = completed.plus(listOf(board))
                completed.count() == toComplete
            }

        return calculate(completedBoard, completeCalls, completeCalls.last())
    }
}
