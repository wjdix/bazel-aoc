package org.softwarecurmudgeon.common

fun <K, V> Map<K, V>.update(key: K, default: V, updater: (V) -> V): Map<K, V> {
    val current = this.getOrDefault(key, default)
    val new = updater(current)
    return this.plus(Pair(key, new))
}

fun Map<List<Char>, Long>.charCounts(): Map<Char, Long> =
    this.toList().fold(mapOf()) { acc, (pair, count: Long) ->
        pair.fold(acc) { counts, char ->
            counts.update(char, 0) { it + count }
        }
    }
