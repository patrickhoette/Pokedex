package com.patrickhoette.pokedex.app.coroutine

import com.patrickhoette.core.utils.coroutine.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object DefaultDispatcherProvider : DispatcherProvider {

    override val Main: CoroutineDispatcher
        get() = Dispatchers.Main

    override val IO: CoroutineDispatcher
        get() = Dispatchers.IO

    override val Default: CoroutineDispatcher
        get() = Dispatchers.Default

    override val Unconfined: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}
