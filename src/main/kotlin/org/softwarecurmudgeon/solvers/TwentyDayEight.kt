package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day
import org.softwarecurmudgeon.compy.Compy
import org.softwarecurmudgeon.compy.Instruction as CompyInstruction

object TwentyDayEight: Solution<CompyInstruction, Int>(), Solver {
    override val day: Day
        get() = Day(2020, 8)

    override fun parseInput(input: Sequence<String>): Sequence<CompyInstruction> =
        input
            .filter(String::isNotEmpty)
            .map{ CompyInstruction.parse(it) }

    override fun partOne(input: Sequence<CompyInstruction>): Int =
        generateSequence(
            seed = Compy(input.toList()),
            nextFunction = { it.execute() }
        ).first(Compy::looped).previous

    override fun partTwo(input: Sequence<CompyInstruction>): Int {
        TODO("Not yet implemented")
    }
}
