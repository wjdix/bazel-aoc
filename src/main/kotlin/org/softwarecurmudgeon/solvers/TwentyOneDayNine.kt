package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

typealias BasinMap = Pair<List<List<Int>>, List<Set<Coords>>>

object TwentyOneDayNine: Solution<List<Int>, Int>(), Solver {
    override val day: Day
        get() = Day(2021, 9)

    override fun parseInput(input: Sequence<String>): Sequence<List<Int>> =
        input
            .filter(String::isNotEmpty)
            .map {
                it.trim().split("").filter(String::isNotEmpty).map(String::toInt)
            }

    override fun partOne(input: Sequence<List<Int>>): Int =
        lowpoints(input.toList())
            .map { coords ->
                input.toList()[coords.y][coords.x]
            }
            .sumOf { it + 1}

    private fun lowpoints(input: List<List<Int>>): List<Coords> =
        0.until(input.count()).flatMap { y ->
            0.until(input.first().count()).mapNotNull { x ->
                val point = input[y][x]
                val neighbors = Coords(y = y, x = x).neighbors()
                    .map { neighbor ->
                        input.getOrNull(neighbor.y)?.getOrNull(neighbor.x) ?: Int.MAX_VALUE
                    }
                if (neighbors.all { point < it }) {
                    Coords(x = x, y = y)
                } else {
                    null
                }
            }
        }

    override fun partTwo(input: Sequence<List<Int>>): Int =
        generateSequence(
            seed = BasinMap(input.toList(), lowpoints(input.toList()).map { setOf(it) }),
            nextFunction = { (input, basins) ->
                growBasins(input, basins)
            },
        )
            .zipWithNext()
            .first { (first, next) -> first == next }
            .first
            .let { (_, basins) ->
                basins
                    .sortedByDescending { it.count() }
                    .take(3)
                    .map(Set<Coords>::count)
                    .reduce(Int::times)
            }

    private fun growBasins(input: List<List<Int>>, basins: List<Set<Coords>>): BasinMap {
        val newBasins = basins.map { basin ->
            val newNeighbors = basin.flatMap { it.neighbors() }
                .filterNot { it in basin }
                .filterNot { neighbor ->
                    input.getOrNull(neighbor.y)?.getOrNull(neighbor.x).let {
                        it == null || it == 9
                    }
            }
            basin.plus(newNeighbors)
        }
        return BasinMap(input, newBasins)
    }
}
