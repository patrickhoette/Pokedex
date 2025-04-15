@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.patrickhoette.core.utils.coroutine

import com.patrickhoette.core.utils.collection.Diff
import com.patrickhoette.core.utils.collection.Diff.Addition
import com.patrickhoette.core.utils.collection.MapDiffer
import com.patrickhoette.core.utils.extension.runningHistory
import kotlinx.coroutines.flow.*

interface MapFlow<K, out V> {

    val snapshot: Map<K, V>

    operator fun get(key: K): Flow<V?>

    fun keys(): Flow<Set<K>>

    fun values(): Flow<Collection<V>>

    fun entries(): Flow<Map<K, V>>

    operator fun contains(key: K): Boolean

    fun observeContains(key: K): Flow<Boolean>

    fun isEmpty(): Boolean

    fun observeIsEmpty(): Flow<Boolean>

    fun isNotEmpty(): Boolean

    fun observeIsNotEmpty(): Flow<Boolean>

    fun diffs(): Flow<Set<Diff<Pair<K, V>>>>
}

class ReadOnlyMapFlow<K, V>(
    private val mutable: MutableMapFlow<K, V>,
) : MapFlow<K, V> by mutable

class MutableMapFlow<K, V>(
    initialValue: Map<K, V> = emptyMap(),
    private val differ: MapDiffer<K, V> = MapDiffer(),
) : MapFlow<K, V> {

    private val _flow = MutableStateFlow(initialValue)
    private val flow = _flow.asStateFlow()

    override val snapshot: Map<K, V>
        get() = flow.value

    override fun get(key: K): Flow<V?> = flow
        .mapLatest { it[key] }
        .distinctUntilChanged()

    override fun keys(): Flow<Set<K>> = flow
        .mapLatest { it.keys }
        .distinctUntilChanged()

    override fun values(): Flow<Collection<V>> = flow.mapLatest { it.values }

    override fun entries(): Flow<Map<K, V>> = flow

    override fun isEmpty(): Boolean = flow.value.isEmpty()

    override fun observeIsEmpty(): Flow<Boolean> = flow
        .mapLatest { it.isEmpty() }
        .distinctUntilChanged()

    override fun isNotEmpty(): Boolean = flow.value.isNotEmpty()

    override fun observeIsNotEmpty(): Flow<Boolean> = flow
        .mapLatest { it.isNotEmpty() }
        .distinctUntilChanged()

    override fun diffs(): Flow<Set<Diff<Pair<K, V>>>> = flow
        .runningHistory()
        .mapLatest { (old, new) ->
            if (old == null) {
                new.map { Addition(it.toPair()) }.toSet()
            } else {
                differ.diff(old, new)
            }
        }
        .drop(1)

    override fun contains(key: K): Boolean = key in flow.value

    override fun observeContains(key: K): Flow<Boolean> = flow
        .mapLatest { key in it }
        .distinctUntilChanged()

    fun update(updater: (Map<K, V>) -> Map<K, V>?) = _flow.update { updater(it).orEmpty() }

    fun updateEntry(key: K, updater: (V?) -> V?) = update {
        val old = it[key]
        val new = updater(old)
        if (new != null) it + (key to new) else it - key
    }

    operator fun plusAssign(entry: Pair<K, V>) = update { it + entry }

    operator fun plusAssign(entries: Map<K, V>) = update { it + entries }

    operator fun minusAssign(key: K) = update { it - key }

    operator fun minusAssign(keys: Collection<K>) = update { it - keys.toSet() }

    operator fun set(key: K, value: V) = update { it + (key to value) }

    fun clear() {
        _flow.value = emptyMap()
    }

    fun asMapFlow() = ReadOnlyMapFlow(this)
}
