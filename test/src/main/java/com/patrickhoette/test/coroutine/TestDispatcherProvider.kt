@file:Suppress("MemberVisibilityCanBePrivate")

package com.patrickhoette.test.coroutine

import com.patrickhoette.core.utils.coroutine.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher

class TestDispatcherProvider : DispatcherProvider {

    val scheduler = TestCoroutineScheduler()
    private val dispatcher = UnconfinedTestDispatcher(scheduler)

    override val Main: CoroutineDispatcher
        get() = dispatcher

    override val IO: CoroutineDispatcher
        get() = dispatcher

    override val Default: CoroutineDispatcher
        get() = dispatcher

    override val Unconfined: CoroutineDispatcher
        get() = dispatcher
}
