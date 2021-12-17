package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day
import kotlin.math.absoluteValue

data class TargetArea(
    val xTarget: IntRange,
    val yTarget: IntRange
)

data class Probe(
    val position: Coords = Coords(0, 0),
    val velocity: Slope
) {
    fun potentialPositions(): Pair<IntRange, IntRange> {
        val maxX = triangle(velocity.x.absoluteValue)
        val xRange = if (velocity.x < 0) {
            (position.x - maxX)..position.x
        } else {
            position.x..(position.x + maxX)
        }
        val yRange = if (velocity.y < 0) {
            -1000..position.y
        } else {
            -1000..(position.y + triangle(velocity.y))
        }
        return Pair(xRange, yRange)
    }
    fun step(): Probe =
        copy(
            position = position.plus(velocity),
            velocity = Slope(
                x = if (velocity.x > 0) {
                    velocity.x - 1
                } else if (velocity.x < 0) {
                    velocity.x + 1
                } else {
                    0
                },
                y = velocity.y -1
            )
        )
    fun canHit(targetArea: TargetArea): Boolean {
        val (xRange, yRange) = potentialPositions()
        return targetArea.xTarget.intersect(xRange).isNotEmpty() &&
                targetArea.yTarget.intersect(yRange).isNotEmpty()
    }

    fun hit(targetArea: TargetArea): Boolean = position.x in targetArea.xTarget && position.y in targetArea.yTarget

    fun hits(targetArea: TargetArea): Boolean =
        generateSequence(this) {
            it.step()
        }
            .takeWhile { it.canHit(targetArea) }
            .any { it.hit(targetArea) }
}

object TwentyOneDaySeventeen: Solution<TargetArea, Int>(), Solver {
    override val day: Day
        get() = Day(2021, 17)

    override fun parseInput(input: Sequence<String>): Sequence<TargetArea> =
        input
            .filter(String::isNotEmpty)
            .map(String::trim)
            .map { line ->
                "target area: x=(-?\\d+)..(-?\\d+), y=(-?\\d+)..(-?\\d+)".toRegex()
                    .matchEntire(line)!!.groupValues.let { (_, xFirst, xLast, yFirst, yLast) ->
                        TargetArea(
                            xTarget = xFirst.toInt()..xLast.toInt(),
                            yTarget = yFirst.toInt()..yLast.toInt()
                        )
                    }
            }

    private fun maxHeight(velocity: Slope) = triangle(velocity.y)

    private const val maxX = 276
    private const val maxY = 76

    override fun partOne(input: Sequence<TargetArea>): Int =
        1.until(maxX).flatMap { x ->
            1.until(maxY).map {y ->
                Probe(
                    velocity = Slope(x = x, y = y)
                )
            }
        }
            .asSequence()
            .filter { it.canHit(input.first()) }
            .filter { it.hits(input.first()) }
            .maxOf { maxHeight(it.velocity) }

    override fun partTwo(input: Sequence<TargetArea>): Int =
        1.until(maxX).flatMap { x ->
            (-75).until(maxY).map {y ->
                Probe(
                    velocity = Slope(x = x, y = y)
                )
            }
        }
            .asSequence()
            .filter { it.canHit(input.first()) }
            .filter { it.hits(input.first()) }
            .count()

}

private fun triangle(n: Int): Int = (n * (n + 1)) / 2
