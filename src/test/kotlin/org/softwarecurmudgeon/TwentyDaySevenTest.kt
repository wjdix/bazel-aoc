package org.softwarecurmudgeon.solvers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class TwentyDaySevenTest {
    private val sampleInput = """
        light red bags contain 1 bright white bag, 2 muted yellow bags.
        dark orange bags contain 3 bright white bags, 4 muted yellow bags.
        bright white bags contain 1 shiny gold bag.
        muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
        shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
        dark olive bags contain 3 faded blue bags, 4 dotted black bags.
        vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
        faded blue bags contain no other bags.
        dotted black bags contain no other bags.
    """.trimIndent().lineSequence()

    @Test
    fun testParseLine() {
        assertEquals(
            BagSpecification(
                name = "light red",
                bags = setOf(
                    BagCount(1, "bright white"),
                    BagCount(2, "muted yellow")
                )
            ),
            BagSpecification.parse(sampleInput.first())
        )
    }

    @Test
    fun testPartOne() {
        assertEquals(
            4,
            TwentyDaySeven.parseInput(sampleInput).let(TwentyDaySeven::partOne)
        )
    }

    @Test
    fun testPartTwo() {
        val sampleInput = """
           shiny gold bags contain 2 dark red bags.
           dark red bags contain 2 dark orange bags.
           dark orange bags contain 2 dark yellow bags.
           dark yellow bags contain 2 dark green bags.
           dark green bags contain 2 dark blue bags.
           dark blue bags contain 2 dark violet bags.
           dark violet bags contain no other bags. 
        """.trimIndent().lineSequence()

        assertEquals(
            126,
            TwentyDaySeven.parseInput(sampleInput).let(TwentyDaySeven::partTwo)
        )
    }
}
