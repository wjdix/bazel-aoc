package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day
import org.softwarecurmudgeon.common.update

data class Game(
    val player1Position: Int,
    val player2Position: Int,
    val player1Score: Long = 0L,
    val player2Score: Long = 0L,
    val rolls: Int = 0,
    val playerTurn: Int = 0,
) {
    fun winner(): Int = if (player1Score > player2Score) {
        0
    } else {
        1
    }
    fun isComplete(finalScore: Long) = player1Score >= finalScore || player2Score >= finalScore
    fun result(): Long {
        println(this)
        return player1Score.coerceAtMost(player2Score) * rolls
    }
    private fun ringIt(i: Int) = if (i > 10) {
        if (i.rem(10) == 0) {
            10
        } else {
            i.rem(10)
        }
    } else {
        i
    }
    fun roll(die: Sequence<Int>): Pair<Game, Sequence<Int>> {
        val rollSum = die.take(3).sum()
        val newDie = die.drop(3)
        return Pair(
            rollWithDieResult(rollSum),
            newDie
        )
    }

    private fun rollWithDieResult(rollSum: Int): Game =
        when (playerTurn)  {
            0 -> {
                val newPosition = ringIt(player1Position + rollSum)
                assert(newPosition < 11)
                    copy(
                        player1Position = newPosition,
                        player1Score = player1Score + newPosition,
                        rolls = rolls + 3,
                        playerTurn = 1,
                    )
            }
            1 -> {
                val newPosition = ringIt(player2Position + rollSum)
                assert(newPosition < 11)
                    copy(
                        player2Position = newPosition,
                        player2Score = player2Score + newPosition,
                        rolls = rolls + 3,
                        playerTurn = 0,
                    )
            }
            else -> { throw IllegalArgumentException("FUCK") }
        }

    private val allRolls: List<Pair<Int, Int>> = listOf(
        Pair(3, 1), Pair(4, 3), Pair(5, 6), Pair(6, 7), Pair(7, 6), Pair(8, 3), Pair(9, 1)
    )

    fun rollWithQuantumDice(): List<Pair<Int, Game>> =
        if (isComplete(21)) {
            emptyList()
        } else {
            allRolls.map { (roll, multiplier) ->
                Pair(multiplier, rollWithDieResult(roll))
            }
        }
}

object TwentyOneDayTwentyOne : Solution<Game, Long>(), Solver {
    override val day: Day
        get() = Day(2021, 21)

    override fun parseInput(input: Sequence<String>): Sequence<Game> {
        return sequenceOf(
            Game(
                player1Position = 4,
                player2Position = 7
            )
        )
    }

    override fun partOne(input: Sequence<Game>): Long {
        val die = generateSequence(1.until(101)) { it }.flatten()
        return generateSequence(Pair(input.first(), die)) { (newGame, die) ->
            newGame.roll(die)
        }
            .map(Pair<Game,Sequence<Int>>::first)
            .first { it.isComplete(1000) }
            .result()
    }

    override fun partTwo(input: Sequence<Game>): Long =
        generateSequence(
            Pair(
                mapOf(0 to 0L, 1 to 0L),
                listOf(
                    Pair(1L, input.first())
                )
            )
        ) { (complete: Map<Int, Long>, incomplete) ->
            val (multiplier, toCalculate) = incomplete.first()
            assert(multiplier > 0, { "$multiplier was less than 0" })
            val next = toCalculate.rollWithQuantumDice()
            assert(next.count() > 1)
            val (newComplete, newIncomplete) = next.partition { it.second.isComplete(21) }
            val winners = newComplete.map { Pair(it.first, it.second.winner()) }
            val player1 = winners.filter { it.second == 0 }.sumOf { it.first } * multiplier
            val player2 = winners.filter { it.second == 1 }.sumOf { it.first } * multiplier

            Pair(
                complete
                    .update(0, 0) { it + player1 }
                    .update(1, 0)  { it + player2 },
                newIncomplete.map { (m, game) -> Pair(m * multiplier, game) } +
                        incomplete.minus(Pair(multiplier, toCalculate))
            )
        }
            .first { it.second.isEmpty() }
            .first
            .values
            .maxOrNull()!!
}
