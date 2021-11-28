package org.softwarecurmudgeon.common

typealias LifeMap = List<List<Char>>
typealias NeighborCounter = (input: LifeMap, x: Int, y: Int) -> Int

class GameOfLife(
    val neighborCounter: NeighborCounter,
    private val permissibleNeighbors: Int,
) {
    private fun step(
        input: LifeMap,
    ) : LifeMap =
        input.mapIndexed { y, line ->
            line.mapIndexed { x, char ->
                val neighbors = neighborCounter(
                    input,
                    x,
                    y,
                )
                when (char) {
                    '.' -> '.'
                    '#' -> {
                        if (neighbors >= permissibleNeighbors) {
                            'L'
                        } else {
                            '#'
                        }
                    }
                    'L' -> {
                        if (neighbors == 0) {
                            '#'
                        } else {
                            'L'
                        }
                    }
                    else -> throw IllegalArgumentException("Invalid char")
                }
            }
        }

    fun findFixedPoint(input: LifeMap) =
        generateSequence(
            seed = input,
            nextFunction = { step(input) },
        )
            .zipWithNext()
            .first { (first, next) -> first == next }
            .first
}
