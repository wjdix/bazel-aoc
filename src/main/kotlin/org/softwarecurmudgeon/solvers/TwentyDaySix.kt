package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.BlankLineSeparatedSequence
import org.softwarecurmudgeon.common.Day

data class Answer(val answers: Set<Char>, val answersPerPerson: List<Set<Char>>) {
    fun answeredByAll(): Set<Char> = answersPerPerson.reduce(Set<Char>::intersect)
    companion object {
        fun parse(input: Sequence<String>): Answer =
            input.map(String::toSet).toList().let { answersPerPerson ->
                Answer(
                    answers = answersPerPerson.reduce { acc, answers -> acc.plus(answers) },
                    answersPerPerson = answersPerPerson
                )
            }
    }
}

object TwentyDaySix: Solution<Answer, Int>(), Solver {
    override val day: Day
        get() = Day(2020, 6)

    override fun parseInput(input: Sequence<String>): Sequence<Answer> =
        BlankLineSeparatedSequence
            .generate(input)
            .map(Answer.Companion::parse)

    override fun partOne(input: Sequence<Answer>): Int =
        input.sumOf { it.answers.count() }

    override fun partTwo(input: Sequence<Answer>): Int =
        input.sumOf{ it.answeredByAll().count() }
}
