package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

typealias Edge = Pair<CaveName, CaveName>
typealias Path = List<CaveName>
typealias CaveName = String

object TwentyOneDayTwelve: Solution<Edge, Long>(), Solver {
    override val day: Day
        get() = Day(2021, 12)

    override fun parseInput(input: Sequence<String>): Sequence<Edge> =
        input
            .filter(String::isNotEmpty)
            .map(String::trim)
            .map { it.split("-") }
            .flatMap { (a, b) ->
                listOf(
                    Pair(a, b),
                    Pair(b, a),
                )
            }

    private tailrec fun generatePaths(input: List<Edge>, candidates: List<Path>, completed: List<Path>) : List<Path> =
        if (candidates.isEmpty()) {
            completed
        } else {
            val candidate = candidates.first()
            val rest = candidates.drop(1)

            val (newCompleted, newCandidates) = findNext(candidate, input).partition { completedPath(it) }

            generatePaths(input, rest.plus(newCandidates), completed.plus(newCompleted))
        }

    private fun completedPath(path: Path): Boolean =
        path.last() == "end"

    private fun findNext(candidate: Path, input: List<Edge>): List<Path> {
        val last = candidate.last()
        fun Path.canBeAdded(edge: Edge): Boolean =
            edge.second != "start" &&
                    (edge.second.first().isUpperCase() ||
                    this.filter { it.first().isLowerCase() }.groupBy { it }.all { (_, v) -> v.count() == 1 }
                    || !this.contains(edge.second))

        val nexts = input
            .filter { it.first == last }
            .filter(candidate::canBeAdded)
        return nexts.map {
            candidate.plus(it.second)
        }
    }

    override fun partOne(input: Sequence<Edge>): Long =
        generatePaths(input.toList(), listOf(listOf("start")), listOf()).count().toLong()

    override fun partTwo(input: Sequence<Edge>): Long =
        generatePaths(input.toList(), listOf(listOf("start")), listOf())
            .count().toLong()
}
