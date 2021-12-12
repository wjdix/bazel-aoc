package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

object TwentyOneDayEleven: Solution<MutableList<Int>, Long>(), Solver {
    override val day: Day
        get() = Day(2021, 11)

    override fun parseInput(input: Sequence<String>): Sequence<MutableList<Int>> =
        input
            .filter(String::isNotEmpty)
            .map { line ->
                line.split("").mapNotNull(String::toIntOrNull).toMutableList()
            }

    data class State(
        val step: Int = 0,
        val flashed: Long = 0L,
        val currentStepFlashers: Set<Coords> = emptySet(),
        val flashedOnStep: Boolean = false,
    )

    private fun generateSteps(input: Sequence<MutableList<Int>>): Sequence<State> =
        input.toMutableList().let { map ->
            generateSequence(State()) { state ->
                val flashers = 0.until(10).flatMap{ y ->
                    0.until(10).mapNotNull { x ->
                        if (!state.flashedOnStep) {
                            map[y][x] += 1
                        }
                        if (map[y][x] > 9) {
                            Coords(y = y, x = x)
                        } else {
                            null
                        }
                    }
                }
                    .filter { it !in state.currentStepFlashers }
                if (flashers.isEmpty()) {
                    state.currentStepFlashers.forEach { coords ->
                        map[coords.y][coords.x] = 0
                    }
                    state.copy(
                        flashedOnStep = false,
                        step = state.step + 1,
                        currentStepFlashers = emptySet()
                    )
                } else {
                    flashers.forEach{ flasher ->
                        map[flasher.y][flasher.x] = 0
                        flasher.allNeighbors()
                            .filter { it !in state.currentStepFlashers }
                            .forEach { neighbor ->
                                map[neighbor.y][neighbor.x] += 1
                            }
                    }

                    state.copy(
                        flashed = state.flashed + flashers.count(),
                        currentStepFlashers = state.currentStepFlashers.plus(flashers),
                        flashedOnStep = true,
                    )
                }
            }
        }

    fun flashesAfterSteps(n: Int, input: Sequence<MutableList<Int>>): Long =
        generateSteps(input).first { it.step == n }
            .flashed

    override fun partOne(input: Sequence<MutableList<Int>>): Long =
        flashesAfterSteps(100, input)

    override fun partTwo(input: Sequence<MutableList<Int>>): Long =
        generateSteps(input).first { it.currentStepFlashers.count() == 100 }.step.toLong() + 1
}

private fun Coords.allNeighbors(): List<Coords> =
    listOf(-1, 0, 1).flatMap { y ->
        listOf(-1, 0, 1).map { x ->
            plus(Slope(y = y, x = x))
        }
    }
        .filter { it.x in (0..9) && it.y in (0..9) && it != this }
