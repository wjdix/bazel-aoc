package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

sealed interface CrtInstruction {
    val cycles: Int
}

object Noop : CrtInstruction {
    override val cycles: Int
        get() = 1
}

data class Addx(val value: Int): CrtInstruction {
    override val cycles: Int
        get() = 2
}

data class CrtComputer(
    val register: Int = 1,
    val currentInstruction: CrtInstruction? = null,
    val cyclesLeft: Int = 0
) {
    fun tick(instructions: List<CrtInstruction>): Pair<CrtComputer, List<CrtInstruction>> {
        return when(this.currentInstruction) {
            null -> {
                val nextInstruction = instructions.first()
                this.copy(
                    currentInstruction = nextInstruction,
                    cyclesLeft = nextInstruction.cycles
                ).tick(instructions.drop(1))
            }
            Noop -> {
                Pair(
                    this.copy(
                        currentInstruction = null
                    ),
                    instructions
                )
            }
            is Addx -> {
                when(this.cyclesLeft) {
                    0 -> {
                        this.copy(
                            register = this.currentInstruction.value + this.register,
                            currentInstruction = null,
                            cyclesLeft = 0
                        ).tick(instructions)
                    }
                    else -> {
                        Pair(
                            this.copy(
                                cyclesLeft = this.cyclesLeft - 1
                            ),
                            instructions
                        )
                    }
                }
            }
        }
    }
}

object TwentyTwoDayTen: Solution<CrtInstruction, Int>(), Solver {
    override val day: Day
        get() = Day(year = 2022, day = 10)

    override fun parseInput(input: Sequence<String>): Sequence<CrtInstruction> =
        input
            .filter(String::isNotEmpty)
            .map { line ->
                val splitLine = line.split(" ")
                when (splitLine[0]) {
                    "noop" -> Noop
                    else -> Addx(splitLine[1].toInt())
                }
            }

    override fun partOne(input: Sequence<CrtInstruction>): Int =
        generateSequence(
            seedFunction = {
                CrtComputer().tick(instructions = input.toList())
            },
            nextFunction = {(computer, instructions) ->
                computer.tick(instructions)
            }
        )
            .takeWhile { (_, instructions) -> instructions.isNotEmpty() }
            .map { it.first }
            .mapIndexedNotNull {i, computer ->
                if ((i + 21).mod(40) == 0) {
                    (i+1) * computer.register
                } else {
                    null
                }
            }
            .let { println(it.toList()); it}
            .reduce(Int::plus)

    override fun partTwo(input: Sequence<CrtInstruction>): Int {
        println("output")
        generateSequence(
            seedFunction = {
                CrtComputer().tick(instructions = input.toList())
            },
            nextFunction = {(computer, instructions) ->
                computer.tick(instructions)
            }
        )
            .takeWhile { (_, instructions) -> instructions.isNotEmpty() }
            .map { it.first }
            .mapIndexed { i, computer ->
                if (i.mod(40) in listOf(computer.register - 1, computer.register, computer.register + 1)) {
                    '#'
                } else {
                    '.'
                }
            }.windowed(size = 40, step = 40, partialWindows = true)
            .joinToString("\n") {it.joinToString("")}
            .let(::println)


        return 4
    }
}
