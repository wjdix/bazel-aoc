package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.BlankLineSeparatedSequence
import org.softwarecurmudgeon.common.Day

data class CraneInstruction(
    val from: Int,
    val to: Int,
    val number: Int,
)

class CraneState(
    val stacks: MutableList<MutableList<Char>>
) {
    fun apply(instruction: CraneInstruction): CraneState {
        val move = stacks[instruction.from - 1].takeLast(instruction.number).reversed()
        stacks[instruction.from - 1] = stacks[instruction.from - 1].dropLast(instruction.number).toMutableList()
        stacks[instruction.to - 1] = stacks[instruction.to -1].plus(move).toMutableList()
        return this
    }
    fun applyTwo(instruction: CraneInstruction): CraneState {
        val move = stacks[instruction.from - 1].takeLast(instruction.number)
        stacks[instruction.from - 1] = stacks[instruction.from - 1].dropLast(instruction.number).toMutableList()
        stacks[instruction.to - 1] = stacks[instruction.to -1].plus(move).toMutableList()
        return this
    }

    fun top(): String {
        return stacks.map { it.last() }.joinToString("")
    }
}

object TwentyTwoDayFive: Solution<Sequence<String>, String>(), Solver {
    override val day: Day
        get() = Day(year = 2022, day = 5)

    override fun parseInput(input: Sequence<String>): Sequence<Sequence<String>> {
        return BlankLineSeparatedSequence.generate(input)
    }

    private fun parseState(stateDescription: Sequence<String>): CraneState {
        val size = stateDescription.toList().reversed().first().trim().split(" ").last().toInt()
        val state = stateDescription.toList().reversed().drop(1).map {
            it.windowed(size = 4, step = 4, partialWindows = true).toList()
        }.toList()
        return CraneState(
            (0 until size).map { column ->
                state
                    .map { it[column] }
                    .takeWhile { it.isNotBlank() }
                    .map{ it[1] }
                    .toMutableList()
            }.toMutableList()
        )
    }

    private fun parseInstructions(instructions: Sequence<String>): Sequence<CraneInstruction> =
        instructions.map(::parseInstruction)

    private fun parseInstruction(s: String): CraneInstruction {
        val split = s.split(" ")
        return CraneInstruction(
            number = split[1].toInt(),
            from = split [3].toInt(),
            to = split[5].toInt(),
        )
    }

    override fun partTwo(input: Sequence<Sequence<String>>): String {
        val state = parseState(input.first())
        val instructions = parseInstructions(input.toList()[1])

        return instructions.fold(state) { acc, instruction ->
            acc.applyTwo(instruction)
        }.top()
    }

    override fun partOne(input: Sequence<Sequence<String>>): String {
        val state = parseState(input.first())
        val instructions = parseInstructions(input.toList()[1])

        return instructions.fold(state) { acc, instruction ->
            acc.apply(instruction)
        }.top()
    }
}
