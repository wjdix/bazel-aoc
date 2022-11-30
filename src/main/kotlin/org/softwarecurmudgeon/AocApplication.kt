package org.softwarecurmudgeon

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required
import org.softwarecurmudgeon.common.Day
import org.softwarecurmudgeon.common.InputFetch
import org.softwarecurmudgeon.common.Submitter
import org.softwarecurmudgeon.solvers.Solver
import kotlin.reflect.KClass

class SolutionRouter{
    private val solvers: Map<Day, Solver> =
        Solver::class
            .sealedSubclasses
            .mapNotNull(KClass<out Solver>::objectInstance)
            .associateBy(Solver::day)

    fun routeForDay(day: Day): Solver? = solvers[day]
}

@Suppress("NestedBlockDepth")
object AocApplication {
    @JvmStatic fun main(args: Array<String>) {
        val parser = ArgParser("aoc")
        val year by parser.option(ArgType.Int, shortName = "y", description = "Year").required()
        val day by parser.option(ArgType.Int, shortName = "d", description = "Day").required()
        val part by parser
            .option(
                ArgType.Choice(listOf("1", "2"), { it }),
                shortName = "p",
                description = "Part"
            )
            .required()
        val submit by parser
            .option(ArgType.Boolean, shortName = "s", description = "Submit Solution")
            .default(false)

        parser.parse(args)

        val yearDay = Day(year = year, day = day)

        val solverRouter = SolutionRouter()

        InputFetch.forDay(yearDay).let { input ->
            solverRouter
                .routeForDay(yearDay)
                ?.let { solver ->
                    val solvingFunction = when (part) {
                        "1" -> solver::solvePartOne
                        "2" -> solver::solvePartTwo
                        else -> throw IllegalArgumentException("not a real part: $part")
                    }
                    input
                        .lineSequence()
                        .let(solvingFunction)
                        .let { solution ->
                            println(solution)
                            if (submit) println(Submitter.forDay(yearDay, part, solution))
                        }

                }
                ?: println("No Solver Available for $yearDay")
        }
    }
}
