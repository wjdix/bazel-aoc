package org.softwarecurmudgeon.compy

data class Compy(
    val instructions: List<Instruction>,
    val visited: Set<Int> = setOf(),
    val instructionPointer: Int = 0,
    val accumulator: Int = 0,
    val previous: Int = 0,
) {
    fun execute(): Compy =
        instructions[instructionPointer]
            .let { instruction ->
                when (instruction) {
                    is Accumulator -> this.copy(
                        visited = visited + this.instructionPointer,
                        previous = accumulator,
                        accumulator = accumulator + instruction.operand,
                        instructionPointer = instructionPointer + 1
                    )
                    is Jump -> this.copy(
                        visited = visited + this.instructionPointer,
                        previous = accumulator,
                        instructionPointer = instructionPointer + instruction.operand
                    )
                }
            }
    fun looped(): Boolean = instructionPointer in visited

    fun completed(): Boolean = instructionPointer == instructions.count() + 1
}
