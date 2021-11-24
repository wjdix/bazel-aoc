package org.softwarecurmudgeon.compy

data class Compy(
    val instructions: List<Instruction>,
    val visited: Set<Int> = setOf(),
    val instructionPointer: Int = 0,
    val accumulator: Int = 0,
    val previous: Int = 0,
    val forked: Boolean = false,
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
                    is NoOp -> this.copy(
                        visited = visited + this.instructionPointer,
                        previous = accumulator,
                        instructionPointer = instructionPointer + 1
                    )
                }
            }
    fun looped(): Boolean = instructionPointer in visited

    fun completed(): Boolean = instructionPointer == instructions.count()

    fun executeWithFork(): List<Compy> =
        if (forked) {
            listOf(execute())
        } else {
            instructions[instructionPointer]
                .let { instruction ->
                    when (instruction) {
                        is Jump -> {
                            val newNoop = NoOp(instruction.operand)
                            val newInstructions = instructions.toMutableList()
                            newInstructions[instructionPointer] = newNoop
                            listOf(
                                this.copy(
                                    forked = true,
                                    instructions = newInstructions
                                ).execute(),
                                execute()
                            )
                        }
                        is NoOp -> {
                            val newJump = Jump(instruction.operand)
                            val newInstructions = instructions.toMutableList()
                            newInstructions[instructionPointer] = newJump
                            listOf(
                                this.copy(
                                    forked = true,
                                    instructions = newInstructions
                                ).execute(),
                                execute()
                            )
                        }
                        else -> {
                            listOf(execute())
                        }
                    }
                }
        }
}

data class ForkingExecutor(
    val current: Compy,
    val executions: List<Compy> = emptyList()
) {
    fun next(): ForkingExecutor {
        val nextStates = executions + current.executeWithFork()
        return copy(
            current = nextStates.first(),
            executions = nextStates.drop(1)
        )
    }
}
