package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day
import kotlin.system.measureTimeMillis

sealed interface Solver {
    val day: Day
    fun solvePartOne(input: Sequence<String>): String
    fun solvePartTwo(input: Sequence<String>): String
}

abstract class Solution<T, U> {
    fun solvePartOne(input: Sequence<String>): String {
        val parsed = parseInput(input)
        val answer: String
        val time =  measureTimeMillis {
            answer = partOne(parsed).toString()
        }
        println("Solving took $time ms")
        return answer
    }

    fun solvePartTwo(input: Sequence<String>): String {
        val parsed = parseInput(input)
        val answer: String
        val time =  measureTimeMillis {
            answer = partTwo(parsed).toString()
        }
        println("Solving took $time ms")
        return answer
    }

    abstract fun parseInput(input: Sequence<String>): Sequence<T>
    abstract fun partOne(input: Sequence<T>): U
    abstract fun partTwo(input: Sequence<T>): U
}
