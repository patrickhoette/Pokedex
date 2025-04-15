package com.patrickhoette.core.utils.collection

import com.patrickhoette.core.utils.collection.Diff.*

class MapDiffer<K, V>(
    private val equals: (V, V) -> Boolean = { a, b -> a == b },
) : Differ<Map<K, V>, Pair<K, V>> {

    override fun diff(
        old: Map<K, V>,
        new: Map<K, V>,
    ): Set<Diff<Pair<K, V>>> {
        // Both are empty, no work needed
        if (old.isEmpty() && new.isEmpty()) return emptySet()
        // Only additions
        if (old.isEmpty()) return new.mapTo(linkedSetOf()) { Addition(it.toPair()) }
        // Only removals
        if (new.isEmpty()) return old.mapTo(linkedSetOf()) { Removal(it.toPair()) }

        val returnSet = linkedSetOf<Diff<Pair<K, V>>>()

        // Loop over the old map to find out all the removals and changes and add them to the set in key order of old
        for ((key, oldValue) in old) {
            val newValue = new[key]
            val entry = when {
                newValue != null && !equals(oldValue, newValue) -> Change(
                    old = key to oldValue,
                    new = key to newValue,
                )
                newValue == null -> Removal(key to oldValue)
                else -> null
            }
            if (entry != null) returnSet += entry
        }

        // Add all new additions to the end of the set
        for (addition in new - old.keys) {
            returnSet += Addition(addition.toPair())
        }

        return returnSet
    }
}
