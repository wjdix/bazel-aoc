package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

typealias RiskMap = List<List<Int>>
typealias RiskPath = List<Coords>

object TwentyOneDayFifteen : Solution<RiskMap, Long>(), Solver {
    override val day: Day
        get() = Day(2021, 15)

    override fun parseInput(input: Sequence<String>): Sequence<RiskMap> =
        sequenceOf(
            input
                .filter(String::isNotEmpty)
                .map { line ->
                    line
                        .split("")
                        .map(String::trim)
                        .filter(String::isNotEmpty)
                        .map(String::toInt)
                }
                .toList()
        )

    private fun bestPath(
        goal: Coords,
        cost: (Coords, Coords) -> Int,
        heuristic: (Coords, Coords) -> Int,
    ): RiskPath {
        val openSet = mutableSetOf(Coords(0, 0))
        val cameFrom = mutableMapOf<Coords, Coords>()
        val gScore = mutableMapOf<Coords, Int>().withDefault { Int.MAX_VALUE }
        gScore[Coords(0, 0)] = 0
        val fScore = mutableMapOf<Coords, Int>().withDefault { Int.MAX_VALUE }
        while (openSet.isNotEmpty()) {
            val lowest = openSet.minByOrNull { fScore[it]!! }!!
            if (lowest == goal) {
                return reconstructPath(lowest, cameFrom)
            }
            openSet -= lowest
            lowest.limitedNeighbors(maxX = goal.x, maxY = goal.y).forEach { neighbor ->
                val tentativeGScore = gScore.getOrDefault(lowest, Int.MAX_VALUE) + cost(lowest, neighbor)
                if (tentativeGScore < gScore.getOrDefault(neighbor, Int.MAX_VALUE)) {
                    cameFrom[neighbor] = lowest
                    gScore[neighbor] = tentativeGScore
                    fScore[neighbor] = tentativeGScore + heuristic(neighbor, goal)
                    openSet.add(neighbor)
                }
            }
        }
        return emptyList()
    }

    private tailrec fun reconstructPath(
        lowest: Coords?,
        cameFrom: Map<Coords, Coords>,
        currentPath: RiskPath = emptyList()
    ): RiskPath =
        if (lowest == null) {
            currentPath
        } else {
            val newLowest = cameFrom[lowest]
            reconstructPath(
                newLowest,
                cameFrom,
                if (newLowest == null) {
                    currentPath
                } else {
                    currentPath.plus(newLowest)
                }
            )
        }

    override fun partOne(input: Sequence<RiskMap>): Long {
        val goal = Coords(
            y = input.first().count() - 1,
            x = input.first().first().count() - 1
        )
        return bestPath(
            goal = goal,
            cost = {_, dest ->
                input.first().getOrNull(dest.y)?.getOrNull(dest.x) ?: Int.MAX_VALUE
            },
            heuristic = { source, g ->
                source.distanceTo(g)
            }
        )
            .sumOf {
                input.first().getOrNull(it.y)?.getOrNull(it.x)?.toLong() ?: 0L
            }
    }

    override fun partTwo(input: Sequence<RiskMap>): Long {
        val goal = Coords(
            y = input.first().count() * 5 - 1,
            x = input.first().first().count() * 5 - 1
        )
        return bestPath(
            goal = goal,
            cost = {_, dest ->
                input.first().getRepeated(x = dest.x, y = dest.y)
            },
            heuristic = { source, g ->
                source.distanceTo(g)
            }
        )
            .asReversed()
            .drop(1)
            .plus(goal)
            .sumOf {
                input.first().getRepeated(x = it.x, y = it.y).toLong()
            }
    }
}

fun RiskMap.getRepeated(x: Int, y: Int): Int {
    val maxY = this.count()
    val maxX = this.first().count()
    val repeated = (x / maxX) + (y / maxY)
    return if (repeated < 1) {
        this.getOrNull(y)?.get(x)!!
    } else {
        val current = this.getOrNull(y % maxY)?.get(x % maxX)!!
        val new = current + repeated
        if (new.rem(9) == 0) {
            9
        } else {
            ((current + repeated) % 9)
        }
    }
}
