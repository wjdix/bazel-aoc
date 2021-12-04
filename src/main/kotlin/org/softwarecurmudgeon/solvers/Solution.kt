package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

sealed interface Solver {
    val day: Day
    fun solvePartOne(input: Sequence<String>): String
    fun solvePartTwo(input: Sequence<String>): String
}

abstract class Solution<T, U> {
    fun solvePartOne(input: Sequence<String>): String =
        parseInput(input).let(::partOne).toString()

    fun solvePartTwo(input: Sequence<String>): String =
        parseInput(input).let(::partTwo).toString()

    abstract fun parseInput(input: Sequence<String>): Sequence<T>
    abstract fun partOne(input: Sequence<T>): U
    abstract fun partTwo(input: Sequence<T>): U
}
