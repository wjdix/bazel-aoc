package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BinarySeatTest {
    @Test
    fun testParse() {
        val seat = BinarySeat.parse("BFFFBBFRRR")
        assertEquals(567, seat.seatId)
        assertEquals(70, seat.row)
        assertEquals(7, seat.column)
    }
}
