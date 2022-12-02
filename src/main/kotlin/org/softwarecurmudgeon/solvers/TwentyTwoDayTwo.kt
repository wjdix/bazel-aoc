package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

object TwentyTwoDayTwo : Solution<Pair<Char, Char>, Int>(), Solver {
    override val day: Day
        get() = Day(2022, 2)

    override fun parseInput(input: Sequence<String>): Sequence<Pair<Char, Char>> {
        return input
            .map(String::trim)
            .filterNot(String::isEmpty)
            .map {
                val (first, second) = it.split(" ")
                Pair(first.first(), second.first())
            }
    }

    override fun partTwo(input: Sequence<Pair<Char, Char>>): Int {
        return input.map(::scorePairTwo).sum()
    }

    override fun partOne(input: Sequence<Pair<Char, Char>>): Int {
        return input.map(::scorePair).sum()
    }

    private fun scorePairTwo(input: Pair<Char, Char>): Int {
        val thrown = when (input.first) {
            'A' -> when (input.second) {
                'X' -> 3 //scissors
                'Y' -> 1 // rock
                'Z' -> 2 // paper
                else -> throw RuntimeException("nope")
            }
            'B' -> when (input.second) {
                'X' -> 1 //rock
                'Y' -> 2 // paper
                'Z' -> 3 // scissors
                else -> throw RuntimeException("nope")
            }
            'C' -> when (input.second) {
                'X' -> 2 //paper
                'Y' -> 3 //scissors
                'Z' -> 1
                else -> throw RuntimeException("nope")
            }
            else -> throw RuntimeException("nope")
        }
        val resultScore = when(input.second) {
            'X' -> 0
            'Y' -> 3
            'Z' -> 6
            else -> throw RuntimeException("nope")
        }

        return resultScore + thrown
    }

    private fun scorePair(input: Pair<Char, Char>): Int {
        val throwScore = when (input.second) {
            'X' -> 1
            'Y' -> 2
            'Z' -> 3
            else -> throw RuntimeException("Not expected")
        }
        val winScore = when (input.first) {
            'A' -> when (input.second) {
                'X' -> 3
                'Y' -> 6
                'Z' -> 0
                else -> throw RuntimeException("Not expected")
            }
            'B' -> when (input.second) {
                'X' -> 0
                'Y' -> 3
                'Z' -> 6
                else -> throw RuntimeException("Not expected")
            }
            'C' -> when (input.second) {
                'X' -> 6
                'Y' -> 0
                'Z' -> 3
                else -> throw RuntimeException("Not expected")
            }
            else -> throw RuntimeException("Not expected")
        }
        return winScore + throwScore
    }
}
