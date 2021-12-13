package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.BlankLineSeparatedSequence
import org.softwarecurmudgeon.common.Day


sealed interface Fold {
    data class AlongX(val value: Int): Fold
    data class AlongY(val value: Int): Fold
}

data class Folder(
    val points: Set<Coords>,
    val folds: List<Fold>
) {
    fun fold(): Folder {
        if (folds.isEmpty()) {
            return this
        }
        val fold = this.folds.first()

        return Folder(
            points = points.map{ it.fold(fold) }.toSet(),
            folds = this.folds.drop(1)
        )
    }
}

private fun Coords.fold(fold: Fold): Coords =
    when (fold) {
        is Fold.AlongY -> {
            if (this.y < fold.value) {
                this
            } else {
                copy(
                    y = fold.value - (y - fold.value)
                )
            }
        }
        is Fold.AlongX -> {
            if (this.x < fold.value) {
                this
            } else {
                copy(
                    x = fold.value - (x - fold.value)
                )
            }
        }
    }

object TwentyOneDayThirteen: Solution<Folder, Long>(), Solver {
    override val day: Day
        get() = Day(2021, 13)

    override fun parseInput(input: Sequence<String>): Sequence<Folder> {
        val (points, folds) =
            BlankLineSeparatedSequence
                .generate(input)
                .toList()
        val parsedPoints = points
            .map {
                val (x, y) = it.split(",").filter(String::isNotEmpty).map(String::toInt)
                Coords(x = x, y = y)
            }
            .toList()

        val parsedFolds = folds.map(String::trim).mapNotNull { fold ->
            val (_, xy, int) = "fold along ([xy])=(\\d+)".toRegex().matchEntire(fold)!!.groupValues
            when (xy) {
                "x" -> Fold.AlongX(int.toInt())
                "y" -> Fold.AlongY(int.toInt())
                else -> null
            }
        }
            .toList()

        return sequenceOf(
            Folder(
                points = parsedPoints.toSet(),
                folds = parsedFolds
            )
        )
    }

    private fun foldEm(folder: Folder): Sequence<Folder> =
        generateSequence(
            seed = folder,
            nextFunction = Folder::fold
        )


    override fun partOne(input: Sequence<Folder>): Long =
        foldEm(input.first())
            .drop(1)
            .first()
            .points
            .count()
            .toLong()

    override fun partTwo(input: Sequence<Folder>): Long {
        val folded = foldEm(input.first()).first { it.folds.isEmpty() }.points

        val y = folded.maxOf { it.y }
        val x = folded.maxOf { it.x }

        0.until(y + 1).forEach { yCoord ->
            val line = 0.until(x + 1).map { xCoord ->
                if (folded.contains(Coords(x = xCoord, y = yCoord))) {
                    '#'
                } else {
                    ' '
                }
            }.joinToString("")
            println(line)
        }

        return folded.count().toLong()
    }
}
