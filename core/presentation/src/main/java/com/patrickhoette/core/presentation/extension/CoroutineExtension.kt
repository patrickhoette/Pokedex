@file:Suppress("Unused")

package com.patrickhoette.core.presentation.extension

import com.patrickhoette.core.utils.extension.launchCatching
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun CoroutineScope.cancelChildren() = coroutineContext.cancelChildren()

fun CoroutineScope.launchOnIO(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit,
) {
    launch(context = context + Dispatchers.IO, block = block)
}

fun CoroutineScope.launchOnMain(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit,
) {
    launch(context = context + Dispatchers.Main, block = block)
}

fun CoroutineScope.launchCatchingOnIO(
    onError: (suspend (Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit,
) {
    (this + Dispatchers.IO).launchCatching(onError, block)
}

fun CoroutineScope.launchCatchingOnMain(
    onError: (suspend (Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit,
) {
    (this + Dispatchers.Main).launchCatching(onError, block)
}

fun <T> Flow<T>.flowOnMain(): Flow<T> = flowOn(Dispatchers.Main)

fun <T> Flow<T>.stateInMain(scope: CoroutineScope, initialValue: T, started: SharingStarted = Eagerly): StateFlow<T> =
    stateIn(scope + Dispatchers.Main, started, initialValue)
