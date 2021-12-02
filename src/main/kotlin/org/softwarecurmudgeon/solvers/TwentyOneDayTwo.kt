package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

sealed interface SubInstruction {
    data class Forward(val amount: Int): SubInstruction
    data class Down(val amount: Int): SubInstruction
    data class Up(val amount: Int): SubInstruction
    companion object {
        fun parse(input: String) : SubInstruction? {
            val (instruction, int) = input.split(" ")
            return when (instruction) {
                "forward" -> Forward(int.toInt())
                "down" -> Down(int.toInt())
                "up" -> Up(int.toInt())
                else -> null
            }
        }
    }
}

data class Sub(val position: Int = 0, val depth: Int = 0) {
    fun receive(subInstruction: SubInstruction): Sub =
        when (subInstruction) {
            is SubInstruction.Down -> this.copy(depth = depth + subInstruction.amount)
            is SubInstruction.Forward -> this.copy(position = position + subInstruction.amount)
            is SubInstruction.Up -> this.copy(depth = depth - subInstruction.amount)
        }

    fun calculate() = position * depth
}

data class AimSub(val position: Int = 0, val depth: Int = 0, val aim: Int = 0) {
    fun receive(subInstruction: SubInstruction): AimSub =
        when (subInstruction) {
            is SubInstruction.Down -> this.copy(aim = aim + subInstruction.amount)
            is SubInstruction.Forward -> this.copy(
                position = position + subInstruction.amount,
                depth = depth + aim * subInstruction.amount,
            )
            is SubInstruction.Up -> this.copy(aim = aim - subInstruction.amount)
        }
    fun calculate() = position * depth
}

object TwentyOneDayTwo: Solution<SubInstruction, Int>(), Solver {
    override val day: Day
        get() = Day(2021, 2)

    override fun parseInput(input: Sequence<String>): Sequence<SubInstruction> =
        input
            .filter(String::isNotEmpty)
            .mapNotNull(SubInstruction.Companion::parse)

    override fun partOne(input: Sequence<SubInstruction>): Int =
        input
            .fold(Sub()){ ship, instruction ->
                ship.receive(instruction)
            }
            .calculate()

    override fun partTwo(input: Sequence<SubInstruction>): Int =
        input
            .fold(AimSub()){ ship, instruction ->
                ship.receive(instruction)
            }
            .calculate()
}
