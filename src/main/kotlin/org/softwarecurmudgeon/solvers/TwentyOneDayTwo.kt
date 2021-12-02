package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

sealed interface Instruction {
    companion object {
        fun parse(input: String) : Instruction? {
            val (instruction, int) = input.split(" ")
            return when (instruction) {
                "forward" -> ThisForward(int.toInt())
                "down" -> Down(int.toInt())
                "up" -> Up(int.toInt())
                else -> null
            }
        }

    }
}

data class ThisForward(val amount: Int): Instruction
data class Down(val amount: Int): Instruction
data class Up(val amount: Int): Instruction

data class ThisShip(val position: Int = 0, val depth: Int = 0) {
    fun receive(instruction: Instruction): ThisShip =
        when (instruction) {
            is Down -> this.copy(depth = depth + instruction.amount)
            is ThisForward -> this.copy(position = position + instruction.amount)
            is Up -> this.copy(depth = depth - instruction.amount)
        }

    fun calculate() = position * depth
}

data class AimShip(val position: Int = 0, val depth: Int = 0, val aim: Int = 0) {
    fun receive(instruction: Instruction): AimShip =
        when (instruction) {
            is Down -> this.copy(aim = aim + instruction.amount)
            is ThisForward -> this.copy(
                position = position + instruction.amount,
                depth = depth + aim * instruction.amount,
            )
            is Up -> this.copy(aim = aim - instruction.amount)
        }
    fun calculate() = position * depth
}

object TwentyOneDayTwo: Solution<Instruction, Int>(), Solver {
    override val day: Day
        get() = Day(2021, 2)

    override fun parseInput(input: Sequence<String>): Sequence<Instruction> =
        input
            .filter(String::isNotEmpty)
            .mapNotNull(Instruction.Companion::parse)

    override fun partOne(input: Sequence<Instruction>): Int =
        input
            .fold(ThisShip()){ ship, instruction ->
                ship.receive(instruction)
            }
            .calculate()

    override fun partTwo(input: Sequence<Instruction>): Int =
        input
            .fold(AimShip()){ ship, instruction ->
                ship.receive(instruction)
            }
            .calculate()
}
