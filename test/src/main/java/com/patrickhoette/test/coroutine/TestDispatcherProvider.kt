package com.patrickhoette.test.coroutine

import com.patrickhoette.core.utils.coroutine.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

object TestDispatcherProvider : DispatcherProvider {

    override val Main: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()

    override val IO: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()

    override val Default: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()

    override val Unconfined: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()
}
