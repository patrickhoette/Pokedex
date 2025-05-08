package com.patrickhoette.core.utils.extension

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun CoroutineScope.launchCatching(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    onError: (suspend (Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit,
) {
    launch(context, start) {
        runCatching {
            block()
        }.onFailure {
            if (it is CancellationException) throw it else onError?.invoke(it)
        }
    }
}

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
