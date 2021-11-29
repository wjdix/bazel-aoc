package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day
import kotlin.math.absoluteValue

sealed interface ShipInstruction

data class Left(val degrees: Int): ShipInstruction
data class Right(val degrees: Int): ShipInstruction
data class Forward(val distance: Int): ShipInstruction
data class North(val distance: Int): ShipInstruction
data class South(val distance: Int): ShipInstruction
data class East(val distance: Int): ShipInstruction
data class West(val distance: Int): ShipInstruction

data class ShipPosition(val x: Int = 0, val y: Int = 0) {
    fun plus(offset: Offset) =
        this.copy(x = x + offset.x, y = y + offset.y)
    fun manhattanDistance(): Int =
        x.absoluteValue + y.absoluteValue
}

data class Offset(val x: Int = 0, val y: Int = 0)

data class WaypointVector(val y: Int = -1, val x : Int = 10) {
    fun plus(offset: Offset): WaypointVector = this.copy(y = y + offset.y, x = x + offset.x)
    fun multiply(transform: List<List<Int>>): WaypointVector =
        WaypointVector(
            x = x * transform[0][0] + y * transform[0][1],
            y = x * transform[1][0] + y * transform[1][1]
        )
}

abstract class BaseShip {
    abstract val position: ShipPosition
    abstract fun receiveInstruction(instruction: ShipInstruction): BaseShip
    fun manhattanDistance(): Int = position.manhattanDistance()
}

data class VectorShip(
    override val position: ShipPosition = ShipPosition(),
    val wayPointVector: WaypointVector = WaypointVector()
) : BaseShip() {
    override fun receiveInstruction(instruction: ShipInstruction): VectorShip =
        when (instruction) {
            is Right -> this.copy(
                wayPointVector = turn(wayPointVector, instruction.degrees)
            )
            is Forward -> this.copy(
                position = position.plus(
                    Offset(
                        x = instruction.distance * wayPointVector.x,
                        y = instruction.distance * wayPointVector.y
                    )
                )
            )
            is Left -> this.copy(
                wayPointVector = turn(wayPointVector, -instruction.degrees)
            )
            is East -> this.copy(
                wayPointVector = wayPointVector.plus(Offset(x = instruction.distance))
            )
            is North -> this.copy(
                wayPointVector = wayPointVector.plus(Offset(y = -instruction.distance))
            )
            is South -> this.copy(
                wayPointVector = wayPointVector.plus(Offset(y = instruction.distance))
            )
            is West -> this.copy(
                wayPointVector = wayPointVector.plus(Offset(x = -instruction.distance))
            )
        }

    private val counterclockwise90 = listOf(
        listOf(0, -1),
        listOf(1, 0)
    )

    private val counterclockwise180 = listOf(
        listOf(-1, 0),
        listOf(0, -1)
    )

    private val counterclockwise270 = listOf(
        listOf(0, 1),
        listOf(-1, 0)
    )

    private fun turn(wayPointVector: WaypointVector, degrees: Int): WaypointVector =
        when (degrees % 360) {
            in listOf(90, -270) -> wayPointVector.multiply(counterclockwise90)
            in listOf(180, -180) -> wayPointVector.multiply(counterclockwise180)
            in listOf(270, -90) -> wayPointVector.multiply(counterclockwise270)
            else -> wayPointVector
        }

}

data class Ship(override val position: ShipPosition = ShipPosition(), val direction: Int = 90): BaseShip() {
    override fun receiveInstruction(instruction: ShipInstruction): Ship =
        when (instruction) {
            is Forward -> {
                this.receiveInstruction(translateForward(direction, instruction.distance))
            }
            is Left -> this.copy(
                direction = direction - instruction.degrees
            )
            is Right -> this.copy(
                direction = direction + instruction.degrees
            )
            is North -> this.copy(
                position = position.plus(Offset(y = -instruction.distance))
            )
            is South -> this.copy(
                position = position.plus(Offset(y = instruction.distance))
            )
            is East -> this.copy(
                position = position.plus(Offset(x = instruction.distance, y = 0))
            )
            is West -> this.copy(
                position = position.plus(Offset(x = -instruction.distance))
            )
        }

    private fun translateForward(direction: Int, distance: Int): ShipInstruction =
        when (direction % 360) {
            0 -> North(distance)
            in listOf(90, -270) -> East(distance)
            in listOf(180, -180) -> South(distance)
            in listOf(270, -90) -> West(distance)
            else -> North(0)
        }
}

object TwentyDayTwelve: Solution<ShipInstruction, Int>(), Solver {
    override val day: Day
        get() = Day(2020, 12)

    private fun parseInstruction(input: String) =
        "([LRFWNSE])(\\d+)"
            .toRegex()
            .matchEntire(input)
            ?.groupValues
            ?.let { (_, instruction, numeric) ->
                numeric.toIntOrNull()?.let { number ->
                    when(instruction) {
                        "L" -> Left(number)
                        "R" -> Right(number)
                        "F" -> Forward(number)
                        "N" -> North(number)
                        "S" -> South(number)
                        "E" -> East(number)
                        "W" -> West(number)
                        else -> null
                    }
                }
            }

    override fun parseInput(input: Sequence<String>): Sequence<ShipInstruction> =
        input.mapNotNull(::parseInstruction)

    override fun partOne(input: Sequence<ShipInstruction>): Int =
        input
            .fold(Ship()) { ship, instruction ->
                ship.receiveInstruction(instruction)
            }
            .manhattanDistance()

    override fun partTwo(input: Sequence<ShipInstruction>): Int =
        input
            .fold(VectorShip()) { ship, instruction ->
                val newShip = ship.receiveInstruction(instruction)
                newShip
            }
            .manhattanDistance()
}
