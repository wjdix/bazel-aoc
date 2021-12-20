package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.BlankLineSeparatedSequence
import org.softwarecurmudgeon.common.Day
import org.softwarecurmudgeon.common.update
import kotlin.math.absoluteValue

object ScannerParser {
    fun parse(input: Sequence<String>): Scanner {
        val beacons = input
            .drop(1)
            .map(XyzCoords.Companion::parse)
            .toList()
        return Scanner(beacons)
    }
}

@JvmInline
value class OrientationIndex(val i: Int) {
    init {
        require(i in (0..23))
    }
}

data class Scanner(
    val beacons: List<XyzCoords>,
    val canonicalPosition: XyzCoords? = null
    ) {
    private val distances: Map<XyzCoords, List<Int>> by lazy {
        beacons.fold(mapOf()) {distanceMap, beacon ->
            beacons.minus(beacon).fold(distanceMap) { acc, innerBeacon ->
                acc.update(beacon, emptyList()) {
                    it.plus(beacon.hyperManhattan(innerBeacon))
                }
            }
        }
    }

    fun isOverlapping(other: Scanner): Boolean =
        overlaps(other).count() >= 11

    fun overlaps(other: Scanner): Set<Pair<XyzCoords, XyzCoords>> =
        distances.mapNotNull { (beacon, beaconDistances) ->
            val pair = other.distances.firstNotNullOfOrNull { (otherBeacon, otherDistances) ->
                val sharedDistances = beaconDistances.count { otherDistances.contains(it) }
                if (sharedDistances >= 11) {
                    otherBeacon
                } else {
                    null
                }
            }
            if (pair == null) {
                null
            } else {
                Pair(beacon, pair)
            }
        }
            .toSet()

    fun inferLocationAndOrientation(secondScanner: Scanner): Pair<OrientationIndex, XyzCoords>? =
        if (this.isOverlapping(secondScanner)) {
            val overlaps = this.overlaps(secondScanner)
            0.until(24).firstNotNullOfOrNull { i ->
                val orientationIndex = OrientationIndex(i)
                overlaps.map { (first, second) ->
                    Pair(first, second.reOrientate(orientationIndex))
                }
                    .isConsistentTranslation()?.let { translation ->
                        Pair(orientationIndex, translation)
                    }
            }
        } else {
            null
        }

    fun normalizeScanner(secondScanner: Scanner): Scanner? =
        inferLocationAndOrientation(secondScanner)?.let { (orientationIndex, translation) ->
            val normalizedBeacons = secondScanner.beacons.map {
                it.reOrientate(orientationIndex).plus(translation)
            }
            Scanner(
                beacons = normalizedBeacons,
                canonicalPosition = translation,
            )
        }
}

data class XyzCoords(val x: Int, val y: Int, val z:Int) {
    companion object {
        fun parse(input: String): XyzCoords =
            input.split(",")
                .mapNotNull(String::toIntOrNull)
                .let { (x, y, z) ->
                    XyzCoords(x = x, y = y, z = z)
                }
    }

    fun translation(other: XyzCoords) =
        XyzCoords(
            x = x - other.x,
            y = y - other.y,
            z = z - other.z
        )

    fun plus(other: XyzCoords) =
        XyzCoords(
            x = x + other.x,
            y = y + other.y,
            z = z + other.z
        )

    fun hyperManhattan(other: XyzCoords) =
        (x - other.x).absoluteValue + (y - other.y).absoluteValue + (z - other.z).absoluteValue

    fun reOrientate(orientationIndex: OrientationIndex): XyzCoords =
        changeAxes(orientationIndex.i / 4).rotate(orientationIndex.i.rem(4))

    private fun changeAxes(index: Int): XyzCoords =
        when (index) {
            0 -> this
            1 -> XyzCoords(x = -x, y = y, z = -z)
            2 -> XyzCoords(x = -z, y = y, z = x)
            3 -> XyzCoords(x = z, y = y, z = -x)
            4 -> XyzCoords(x = y, y = -x, z = z)
            5 -> XyzCoords(x = -y, y = x, z = z)
            else -> throw IllegalArgumentException("can't change axes for $index")
        }

    private fun rotate(index: Int): XyzCoords =
        when (index) {
            0 -> this
            1 -> XyzCoords(x = x, y = z, z = -y)
            2 -> XyzCoords(x = x, y = -y, z = -z)
            3 -> XyzCoords(x = x, y = -z, z = y)
            else -> throw IllegalArgumentException("$index is not a valid rotation")
        }
}

object TwentyOneDayNineteen: Solution<Scanner, Int>(), Solver {
    override val day: Day
        get() = Day(2021, 19)

    override fun parseInput(input: Sequence<String>): Sequence<Scanner> =
        BlankLineSeparatedSequence.generate(input).map(ScannerParser::parse)

    private fun normalizeAll(input: Sequence<Scanner>): List<Scanner> {
        val first = input.first().copy(canonicalPosition = XyzCoords(0, 0, 0))
        return generateSequence(Pair(listOf(first), input.drop(1).toList())) { (normalized, rest) ->
            val (newNormalized, toRemove) = rest.firstNotNullOf { nonNormal ->
                normalized.firstNotNullOfOrNull {
                    it.normalizeScanner(nonNormal)?.let { newNormalized ->
                        Pair(newNormalized, nonNormal)
                    }
                }
            }

            Pair(
                normalized.plus(newNormalized),
                rest.minus(toRemove)
            )
        }.first { (_, rest) -> rest.isEmpty() }.first
    }

    override fun partOne(input: Sequence<Scanner>): Int {
        return normalizeAll(input).flatMap { it.beacons }.toSet().count()
    }

    override fun partTwo(input: Sequence<Scanner>): Int {
        val positions = normalizeAll(input).mapNotNull { it.canonicalPosition }
        return positions.flatMap { outer ->
            positions.minus(outer).map { inner ->
                outer.hyperManhattan(inner)
            }
        }
            .maxOrNull()!!
    }
}

fun Iterable<Pair<XyzCoords, XyzCoords>>.isConsistentTranslation(): XyzCoords? {
    val distinct = map { (first, second) ->
        first.translation(second)
    }
        .distinct()
    return if (distinct.count() == 1) {
        distinct.first()
    } else {
        null
    }
}
