package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TwentyOneDaySixteenTest {
    @Test
    fun testPartOneLiteral() {
        assertEquals(
            2021,
            "D2FE28".lineSequence()
                .let(TwentyOneDaySixteen::parseInput)
                .first()
                .literalValue
        )
    }

    @Test
    fun testPartOneOperator() {
        assertEquals(
            2,
            "38006F45291200"
                .lineSequence()
                .let(TwentyOneDaySixteen::parseInput)
                .first()
                .packets
                .count()
        )
    }

    @Test
    fun testPartOneSmall() {
        assertEquals(
        16,
        "8A004A801A8002F478".lineSequence()
            .let(TwentyOneDaySixteen::parseInput)
            .first()
            .versionSum()
        )
    }

    @Test
    fun testAnotherOne() {
        val packet = "EE00D40C823060"
            .lineSequence()
            .let(TwentyOneDaySixteen::parseInput)
            .first()
        assertEquals(7, packet.version)
        assertEquals(3, packet.packets.count())
    }

    @Test
    fun testCalculatePacket() {
        val packet = "C200B40A82"
            .lineSequence()
            .let(TwentyOneDaySixteen::parseInput)
            .first()

        assertEquals(3, packet.calculate())
    }

    @Test
    fun testMultiplyPacket() {
        val packet = "04005AC33890"
            .lineSequence()
            .let(TwentyOneDaySixteen::parseInput)
            .first()
        assertEquals(54, packet.calculate())
    }

    @Test
    fun testMinimumPacket() {
        val packet = "880086C3E88112"
            .lineSequence()
            .let(TwentyOneDaySixteen::parseInput)
            .first()
        assertEquals(7, packet.calculate())
    }

    @Test
    fun testMaximumPacket() {
        val packet = "CE00C43D881120"
            .lineSequence()
            .let(TwentyOneDaySixteen::parseInput)
            .first()
        assertEquals(9, packet.calculate())
    }

    @Test
    fun testLessThanPacket() {
        val packet = "D8005AC2A8F0"
            .lineSequence()
            .let(TwentyOneDaySixteen::parseInput)
            .first()

        assertEquals(1, packet.calculate())
    }

    @Test
    fun testGreaterThanPacket() {
        val packet = "F600BC2D8F"
            .lineSequence()
            .let(TwentyOneDaySixteen::parseInput)
            .first()

        assertEquals(0, packet.calculate())
    }

    @Test
    fun testAnother() {
        val packet = "9C005AC2F8F0"
            .lineSequence()
            .let(TwentyOneDaySixteen::parseInput)
            .first()
        assertEquals(0, packet.calculate())
    }
    @Test
    fun testComplex() {
        val packet = "9C0141080250320F1802104A08"
            .lineSequence()
            .let(TwentyOneDaySixteen::parseInput)
            .first()

        assertEquals(1, packet.calculate())
    }
}
