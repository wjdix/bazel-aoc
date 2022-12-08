package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

typealias Treeline = List<Int>

object TwentyTwoDayEight: Solution<Treeline, Int>(), Solver {
    override val day: Day
        get() = Day(year = 2022, day = 8)

    override fun parseInput(input: Sequence<String>): Sequence<Treeline> =
        input.filter(String::isNotEmpty).map { line ->
            line.map(Char::digitToInt)
        }

    override fun partTwo(input: Sequence<Treeline>): Int {
        val trees = input.toList()
        val maxRow = trees.count()
        val maxColumn = trees.first().count()
        val visibilityAndHeight = Array(maxRow) { row ->
            Array(maxColumn) { column ->
                if (column in listOf(0, maxColumn - 1) || row in listOf(0, maxRow - 1)) {
                    Pair(0, trees[row][column])
                } else {
                    val treeHeight = trees[row][column]
                    Pair(
                        listOf(
                            trees[row].slice(0.until(column)).reversed(),
                            trees[row].slice((column+1).until(maxColumn)),
                            0.until(row).map {
                                trees[it][column]
                            }.reversed(),
                            (row + 1).until(maxColumn).map {
                                trees[it][column]
                            }
                        ).map { viewDistance(treeHeight, it) }
                            .reduce(Int::times),
                        treeHeight
                    )
                }
            }
        }
        return visibilityAndHeight.maxOf { row ->
            row.maxOf { it.first }
        }
    }

    private fun viewDistance(treeHeight: Int, trees: List<Int>): Int {
        val index = trees.indexOfFirst { it >= treeHeight }
        return if (index == -1) {
            trees.size
        } else {
            index + 1
        }
    }

    override fun partOne(input: Sequence<Treeline>): Int {
        val trees = input.toList()
        val maxRow = trees.count()
        val maxColumn = trees.first().count()
        val visibilityAndHeight = Array(maxRow) { row ->
            Array(maxColumn) { column ->
                if (column in listOf(0, maxColumn) || row in listOf(0, maxRow)) {
                    Pair(true, trees[row][column])
                } else {
                    val left = trees[row].slice(0.until(column))
                    val right = trees[row].slice((column+1).until(maxColumn))
                    val up = 0.until(row).map {
                        trees[it][column]
                    }
                    val down = (row + 1).until(maxColumn).map {
                        trees[it][column]
                    }
                    val treeHeight = trees[row][column]
                    Pair(
                        left.all { it < treeHeight } ||
                            right.all { it < treeHeight} ||
                            up.all {it < treeHeight } ||
                            down.all {it < treeHeight},
                        treeHeight
                    )
                }
            }
        }
        return visibilityAndHeight.flatMap { row ->
            row.filter {item ->
                item.first
            }
        }.count()
    }
}
