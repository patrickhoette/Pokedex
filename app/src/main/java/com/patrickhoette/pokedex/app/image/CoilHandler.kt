package com.patrickhoette.pokedex.app.image

import coil3.ImageLoader
import coil3.PlatformContext
import com.patrickhoette.core.utils.coroutine.DispatcherProvider
import org.koin.core.annotation.Factory

@Factory
class CoilHandler(
    private val dispatcherProvider: DispatcherProvider,
) {

    fun createImageLoader(context: PlatformContext) = ImageLoader.Builder(context)
        .fetcherCoroutineContext(dispatcherProvider.IO)
        .decoderCoroutineContext(dispatcherProvider.Default)
        .interceptorCoroutineContext(dispatcherProvider.Default)
        .build()
}
