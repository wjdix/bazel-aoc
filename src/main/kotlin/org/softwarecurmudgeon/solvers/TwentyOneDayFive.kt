package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

typealias Line = Pair<Coords, Coords>

object TwentyOneDayFive: Solution<Line, Int>(), Solver {
    override val day: Day
        get() = Day(2021, 5)

    private fun parseLine(line: String): Line =
        line
            .split("->")
            .map(String::trim)
            .map(Coords::parse)
            .let { (first, second) ->
                Pair(first, second)
            }


    override fun parseInput(input: Sequence<String>): Sequence<Line> =
        input
            .filter(String::isNotEmpty)
            .map(::parseLine)

    override fun partOne(input: Sequence<Line>): Int =
        input
            .flatMap(Line::coords)
            .groupBy { it }
            .count { it.value.count() >= 2 }


    override fun partTwo(input: Sequence<Line>): Int =
        input
            .flatMap(Line::diagonalCoords)
            .groupBy { it }
            .count { it.value.count() >= 2 }
}

private fun  Pair<Coords, Coords>.coords(): List<Coords> =
    if (first.x == second.x) {
        val start = min(first.y, second.y)
        val finish = max(first.y, second.y) + 1
        start.until(finish).map {
            Coords(x = first.x, y = it)
        }
    } else if (first.y == second.y) {
        val start = min(first.x, second.x)
        val finish = max(first.x, second.x) + 1
        start.until(finish).map {
            Coords(x = it, y = first.y)
        }
    } else {
        emptyList()
    }

fun Pair<Coords, Coords>.diagonalCoords(): List<Coords> =
    if (first.x == second.x) {
        val start = min(first.y, second.y)
        val finish = max(first.y, second.y) + 1
        start.until(finish).map {
            Coords(x = first.x, y = it)
        }
    } else if (first.y == second.y) {
        val start = min(first.x, second.x)
        val finish = max(first.x, second.x) + 1
        start.until(finish).map {
            Coords(x = it, y = first.y)
        }
    } else if (
        (first.x - second.x).absoluteValue ==
        (first.y - second.y).absoluteValue
    ) {
        val startX = min(first.x, second.x)
        val finishX = max(first.x, second.x) + 1
        val startY = min(first.y, second.y)
        val finishY = max(first.y, second.y) + 1
        startX.until(finishX).flatMap { x ->
            startY.until(finishY).map { y ->
                Coords(x = x, y = y)
            }
        }
            .filter { candidate ->
                (candidate.x - first.x).absoluteValue ==
                        (candidate.y -first.y).absoluteValue
            }
    } else {
        emptyList()
    }
