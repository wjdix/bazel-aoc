package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

data class Packet(
    val version: Int,
    val typeId: Int,
    val packets: List<Packet>,
    val literalValue: Long? = null,
) {
    fun versionSum(): Int {
        return version + packets.sumOf(Packet::versionSum)
    }

    fun calculate(): Long =
        when(typeId) {
            0 -> {
                packets.sumOf(Packet::calculate)
            }
            1 -> {
                packets.fold(1) { acc, packet -> acc * packet.calculate() }
            }
            2 -> {
                packets.minOf { it.calculate() }
            }
            3 -> {
                packets.maxOf { it.calculate() }
            }
            4 -> {
                literalValue!!
            }
            5 -> {
                val first = packets[0].calculate()
                val second = packets[1].calculate()
                if (first > second) {
                    1
                } else {
                    0
                }
            }
            6 -> {
                val first = packets[0].calculate()
                val second = packets[1].calculate()
                if (first < second) {
                   1
                } else {
                   0
                }
            }
            7 -> {
                val first = packets[0].calculate()
                val second = packets[1].calculate()
                if (first == second) {
                    1
                } else {
                    0
                }
            }
            else -> throw IllegalArgumentException("on ho")
        }
    companion object {
        fun parse(input: String): Pair<Packet?, String> {
            if (input.isEmpty()) {
                return Pair(null, input)
            }
            val version = input.take(3).toInt(2)
            return when (val typeId = input.drop(3).take(3).toInt(2)) {
                4 -> {
                    val chunks = input.drop(6).chunked(5)
                    val lastChunkIndex = chunks.indexOfFirst { it.first() == '0' }
                    val number = chunks
                        .take(lastChunkIndex + 1)
                        .joinToString("") { it.drop(1) }
                        .toLong(2)
                    Pair(
                        Packet(
                            version = version,
                            typeId = typeId,
                            packets = emptyList(),
                            literalValue = number
                        ),
                        chunks.drop(lastChunkIndex + 1).joinToString("")
                    )
                }
                else -> {
                    when (input.drop(6).take(1)) {
                        "1" -> {
                            val subpacketCount = input.drop(7).take(11).toInt(2)
                            val (packets, remainder) = splitPacketsByCount(
                                input.drop(18),
                                subpacketCount = subpacketCount
                            )
                            Pair(
                                Packet(
                                    version = version,
                                    typeId = typeId,
                                    packets = packets
                                ),
                                remainder
                            )
                        }
                        "0" -> {
                            val subpacketLength = input.drop(7).take(15).toInt(2)
                            val (packets, remainder) = splitPacketsByLength(input.drop(22), subpacketLength)
                            Pair(
                                Packet(
                                    version = version,
                                    typeId = typeId,
                                    packets = packets
                                ),
                                remainder
                            )
                        }
                        else -> throw IllegalArgumentException("bad lengthTypeId")
                    }
                }
            }
        }

        private fun splitPacketsByLength(packetBits: String, subpacketLength: Int): Pair<List<Packet>, String> {
            val input = packetBits.take(subpacketLength)
            val firstRemainder = packetBits.drop(subpacketLength)
            val (parsed, remainder) = parse(input)
            val packets = generateSequence(Pair(listOf(parsed), remainder)) {
                val (next, newRemainder) = parse(it.second)
                Pair(
                    it.first.plus(next),
                    newRemainder
                )
            }
                .first { it.second.isEmpty() }
                .first
            return Pair(
                packets.filterNotNull(),
                firstRemainder
            )
        }

        private tailrec fun splitPacketsByCount(
            input: String,
            packets: List<Packet> = emptyList(),
            subpacketCount: Int
        ): Pair<List<Packet>, String> {
            return if (packets.count() == subpacketCount) {
                Pair(packets, input)
            } else {
                val (packet, remainder) = parse(input)
                if (packet == null) {
                    return Pair(packets, remainder)
                } else {
                    splitPacketsByCount(remainder, packets.plus(packet), subpacketCount)
                }
            }
        }
    }
}

object TwentyOneDaySixteen: Solution<Packet, Long>(), Solver {
    override val day: Day
        get() = Day(2021, 16)

    @SuppressWarnings("ComplexMethod")
    fun toHex(input: String): String =
        input.split("").mapNotNull {
            when (it) {
                "0" -> "0000"
                "1" -> "0001"
                "2" -> "0010"
                "3" -> "0011"
                "4" -> "0100"
                "5" -> "0101"
                "6" -> "0110"
                "7" -> "0111"
                "8" -> "1000"
                "9" -> "1001"
                "A" -> "1010"
                "B" -> "1011"
                "C" -> "1100"
                "D" -> "1101"
                "E" -> "1110"
                "F" -> "1111"
                else -> {
                    println("badInput: $it")
                    null
                }
            }
        }
            .joinToString("")

    override fun parseInput(input: Sequence<String>): Sequence<Packet> =
        input
            .filter(String::isNotEmpty)
            .map(::toHex)
            .map(Packet.Companion::parse)
            .map { it.first!! }

    override fun partOne(input: Sequence<Packet>): Long =
        input.first().versionSum().toLong()

    override fun partTwo(input: Sequence<Packet>): Long =
        input.first().calculate()
}
