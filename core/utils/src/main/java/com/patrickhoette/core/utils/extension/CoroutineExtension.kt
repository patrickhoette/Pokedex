package com.patrickhoette.core.utils.extension

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly

fun <T> Flow<T>.flowOnIO(): Flow<T> = flowOn(Dispatchers.IO)

fun CoroutineScope.launchCatching(
    onError: (suspend (Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit,
) {
    launch {
        runCatching {
            block()
        }.onFailure {
            if (it is CancellationException) throw it else onError?.invoke(it)
        }
    }
}

fun <T> Flow<T>.stateInIO(scope: CoroutineScope, initialValue: T, started: SharingStarted = Eagerly): StateFlow<T> =
    stateIn(scope + Dispatchers.IO, started, initialValue)

inline fun <reified T, R> Iterable<Flow<T>>.combine(crossinline transform: suspend (List<T>) -> R): Flow<R> =
    combine(this) { transform(it.toList()) }

inline fun <reified T> Iterable<Flow<T>>.combine(): Flow<List<T>> = combine(this) { it.toList() }

/**
 * Returns a flow that emits the the result of [producer].
 */
inline fun <T> flowOf(crossinline producer: suspend () -> T): Flow<T> = flow { emit(producer()) }

inline fun <T, R> Flow<List<T>>.mapListItems(crossinline transform: suspend (T) -> R): Flow<List<R>> = map { list ->
    list.map { transform(it) }
}
