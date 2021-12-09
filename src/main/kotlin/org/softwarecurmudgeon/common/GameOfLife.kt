package org.softwarecurmudgeon.common

typealias LifeMap<X> = List<List<X>>
typealias NeighborCounter<X> = (input: LifeMap<X>, x: Int, y: Int) -> Int

class CharGameOfLife(
    val neighborCounter: NeighborCounter<Char>,
    private val permissibleNeighbors: Int,
) {
    private fun step(
        input: LifeMap<Char>,
    ) : LifeMap<Char> =
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

    fun findFixedPoint(input: LifeMap<Char>) =
        generateSequence(
            seed = input,
            nextFunction = { step(input) },
        )
            .zipWithNext()
            .first { (first, next) -> first == next }
            .first
}
