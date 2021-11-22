package org.softwarecurmudgeon.common

object BlankLineSeparatedSequence {
    fun generate(input: Sequence<String>): Sequence<Sequence<String>> =
        generateSequence(
            seedFunction = {
                val first = input.takeWhile { it.isNotEmpty() }
                val rest = input.drop(first.count() + 1)
                Pair(first, rest)
            },
            nextFunction = { (_, rest) ->
                if (rest.none()) {
                    null
                } else {
                    val first = rest.takeWhile { it.isNotEmpty() }
                    val newRest = rest.drop(first.count() + 1)
                    Pair(first, newRest)
                }
            }
        )
            .map { it.first }
}
