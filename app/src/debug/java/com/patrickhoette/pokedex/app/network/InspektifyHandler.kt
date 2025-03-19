package com.patrickhoette.pokedex.app.network

import com.patrickhoette.core.source.client.InspectionHandler
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIOEngineConfig
import sp.bvantur.inspektify.ktor.InspektifyKtor

class InspektifyHandler : InspectionHandler {

    override fun installInspection(config: HttpClientConfig<CIOEngineConfig>) {
        config.install(InspektifyKtor)
    }
}
