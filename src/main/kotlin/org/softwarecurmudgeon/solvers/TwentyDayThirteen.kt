package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

data class BusNotes(
    val earliestDeparture: Long,
    val busIds: List<Long?>,
)

object TwentyDayThirteen: Solution<BusNotes, Long>(), Solver {
    override val day: Day
        get() = Day(2020, 13)

    override fun parseInput(input: Sequence<String>): Sequence<BusNotes> {
        val lines = input.take(2).toList()
        return sequenceOf(
            BusNotes(
                earliestDeparture =  lines.first().toLong(),
                busIds = lines[1].split(",").map(String::toLongOrNull)
            )
        )
    }

    override fun partOne(input: Sequence<BusNotes>): Long {
        val busNotes = input.first()
        return busNotes
            .earliestDeparture
            .until(busNotes.earliestDeparture + 100_000)
            .flatMap { departure ->  busNotes.busIds.filterNotNull().map { Pair(departure, it)}}
            .first { (a, b) -> a % b == 0L }
            .let { (a, b) ->
                (a - busNotes.earliestDeparture) * b
            }
    }

    override fun partTwo(input: Sequence<BusNotes>): Long {
        val busNotes = input.first()
        val firstId = busNotes.busIds.first()!!
        return 937.until(100_000_000_000_000_000)
            .asSequence()
            .filter { it % firstId == 0L }
            .map { it.until(it + busNotes.busIds.count()) }
            .first{ departures ->
                departures.zip(busNotes.busIds)
                    .all { (a, b) -> b == null || a % b == 0L}
            }
            .let { println(it) ; it}
            .first()
    }
}
