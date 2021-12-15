package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.LifeMap
import org.softwarecurmudgeon.common.NeighborCounter
import org.softwarecurmudgeon.common.CharGameOfLife
import org.softwarecurmudgeon.common.Day
import kotlin.math.absoluteValue

data class Slope(val x: Int, val y: Int)
data class Coords(val x: Int, val y: Int) {
    val neighborSlopes = listOf(
        Slope(x = 1, y = 0),
        Slope(x = -1, y = 0),
        Slope(x = 0, y = 1),
        Slope(x = 0, y = -1)
    )
    fun plus(slope: Slope) = this.copy(x = x + slope.x, y = slope.y + y)
    fun neighbors(): List<Coords> = neighborSlopes.map{ neighborSlope ->
        plus(neighborSlope)
    }

    fun limitedNeighbors(maxX: Int, maxY: Int): List<Coords>  = neighbors()
        .filter { it.x in (0..maxX) && it.y in (0..maxY) }

    fun distanceTo(other: Coords): Int =
        (this.x - other.x).absoluteValue + (this.y - other.y).absoluteValue

    companion object {
        fun parse(string: String): Coords =
            string
                .split(",")
                .map(String::toInt)
                .let { (x, y) ->
                    Coords(x = x, y = y)
                }
    }
}

object TwentyDayEleven: Solution<List<Char>, Int>(), Solver {
    override val day: Day
        get() = Day(2020, 11)

    private val blockingCoords: MutableMap<Pair<Slope, Coords>, Coords> = mutableMapOf()

    private fun visible(input: LifeMap<Char>, coords: Coords, slope: Slope): Char {
        val visibleCoords = blockingCoords.getOrPut(Pair(slope, coords)) {
            uncachedCoords(input = input, coords = coords.plus(slope), slope = slope)
        }
        return input.getOrNull(visibleCoords.y)?.getOrNull(visibleCoords.x) ?: '.'
    }

    private tailrec fun uncachedCoords(input: LifeMap<Char>, coords: Coords, slope: Slope): Coords =
        when (input.getOrNull(coords.y)?.getOrNull(coords.x)) {
            '#' -> coords
            'L' -> coords
            '.' -> uncachedCoords(input, coords.plus(slope), slope)
            null -> Coords(-1, -1)
            else -> Coords(-1, -1)
        }

    private fun countVisible(input: LifeMap<Char>, x: Int, y: Int): Int =
        (-1..1)
            .flatMap { ySlope ->
                (-1..1).map { xSlope ->
                    Slope(ySlope, xSlope)
                }
            }
            .minus(Slope(0,0))
            .map { slope ->
                visible(input, Coords(x = x, y = y), slope)
            }
            .count { it == '#' }

    private fun countNeighbors(input: LifeMap<Char>, x: Int, y: Int): Int =
        ((y - 1)..(y + 1))
            .flatMap{ yi ->
                ((x - 1)..(x + 1)).map { xi ->
                    Pair(xi, yi)
                }
            }
            .minus(Pair(x,y))
            .map { (xi, yi) ->
                input
                    .getOrNull(yi)
                    ?.getOrNull(xi)
                    ?.let { char ->
                        when (char) {
                            '#' -> 1
                            else -> 0
                        }
                    }
                    ?:0
            }
            .sum()

    private fun seatedCount(input: LifeMap<Char>): Int = input.sumOf { line ->
        line.count { it == '#' }
    }

    override fun parseInput(input: Sequence<String>): Sequence<List<Char>> =
        input
            .filter(String::isNotEmpty)
            .map(String::toList)

    private fun solve(input: LifeMap<Char>, counter: NeighborCounter<Char>, permissibleNeighbors: Int = 4): Int =
        CharGameOfLife(
            neighborCounter = counter,
            permissibleNeighbors = permissibleNeighbors
        ).findFixedPoint(input)
            .let(::seatedCount)


    override fun partOne(input: Sequence<List<Char>>): Int =
        solve(input = input.toList(), counter = ::countNeighbors)

    override fun partTwo(input: Sequence<List<Char>>): Int =
        solve(input = input.toList(), counter = ::countVisible, permissibleNeighbors = 5)
}
