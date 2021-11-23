package org.softwarecurmudgeon.compy

sealed interface Instruction {
    companion object {
        fun parse(input: String) : Instruction =
            input.take(3).let { instruction ->
                when (instruction) {
                    "acc" -> Accumulator(parseNum(input.drop(3)))
                    "jmp" -> Jump(parseNum(input.drop(3)))
                    "nop" -> Jump(1)
                    else -> throw IllegalArgumentException("$instruction is not a valid instruction")
                }
            }

        fun parseNum(numInput: String) =
            " ([-+])(\\d+)"
                .toRegex()
                .matchEntire(numInput)
                ?.groupValues
                ?.let { (_, sign, num) ->
                    when (sign) {
                        "-" -> -num.toInt()
                        "+" -> num.toInt()
                        else -> throw IllegalArgumentException("$numInput is not a valid operand")
                    }
                }
                ?: throw  IllegalArgumentException("$numInput something broke")
    }
}

data class Accumulator(val operand: Int) : Instruction

data class Jump(val operand: Int): Instruction
