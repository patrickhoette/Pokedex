@file:Suppress("PropertyName", "VariableNaming")

package com.patrickhoette.core.utils.coroutine

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {

    val Main: CoroutineDispatcher

    val IO: CoroutineDispatcher

    val Default: CoroutineDispatcher

    val Unconfined: CoroutineDispatcher
}
