package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.BlankLineSeparatedSequence
import org.softwarecurmudgeon.common.Day

data class ImageEnhancer(
    val enhancements: List<Char>,
    val image: List<List<Char>>,
) {
    fun enhance(default: Char): ImageEnhancer {
        val maxX = image.first().count()
        val maxY = image.count()
        val newImage = (-1).until(maxY + 1).map { lineIndex ->
            (-1).until(maxX + 1).map { columnIndex ->
                val position = Coords(x = columnIndex, y = lineIndex)
                val lookup = position.neighborsAndMe().map { neighbor ->
                    when (image.getOrElse(neighbor.y) { listOf() }.getOrElse(neighbor.x) { default }) {
                        '.' -> '0'
                        '#' -> '1'
                        else -> throw IllegalArgumentException("FUCK")
                    }
                }
                    .joinToString("").toInt(2)
                enhancements[lookup]
            }
        }
        return ImageEnhancer(
            enhancements = enhancements,
            image = newImage
        )
    }

    fun countPixels(): Long = image.sumOf { it.count { char -> char == '#'}.toLong() }
}

private fun Coords.neighborsAndMe(): List<Coords> =
    listOf(Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
           Pair(0, -1), Pair(0, 0), Pair(0, 1),
           Pair(1, -1), Pair(1, 0), Pair(1, 1)
    ).map { (yOffset, xOffset) ->
        Coords(x = x + xOffset, y = y + yOffset)
    }

object TwentyOneDayTwenty: Solution<ImageEnhancer, Long>(), Solver {
    override val day: Day
        get() = Day(2021, 20)

    override fun parseInput(input: Sequence<String>): Sequence<ImageEnhancer> {
        val (enhancements, unparsedImage) = BlankLineSeparatedSequence.generate(input).toList()
        return sequenceOf(
            ImageEnhancer(
                enhancements = enhancements.first().toList(),
                image = unparsedImage.map {
                    it.toList()
                }
                    .toList()
            )
        )
    }

    private fun enhance(image: ImageEnhancer): Sequence<Pair<ImageEnhancer, Char>> {
        val first = '.'
        return generateSequence(Pair(image, first)) { (image, default) ->
            val nextDefault = 0.until(9).map { default }.joinToString("") {
                when(it) {
                    '.' -> "0"
                    '#' -> "1"
                    else -> throw IllegalArgumentException("Bad bixel")
                }
            }.toInt(2).let { image.enhancements[it] }
            val newImage = image.enhance(default)
            Pair(newImage, nextDefault)
        }
    }

    override fun partOne(input: Sequence<ImageEnhancer>): Long =
        enhance(input.first())
            .drop(2)
            .first()
            .first
            .countPixels()

    override fun partTwo(input: Sequence<ImageEnhancer>): Long =
        enhance(input.first())
            .drop(50)
            .first()
            .first
            .countPixels()
}

