package com.patrickhoette.pokedex.app.network

import com.patrickhoette.core.source.client.InspectionHandler
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIOEngineConfig

class InspektifyHandler : InspectionHandler {

    override fun installInspection(config: HttpClientConfig<CIOEngineConfig>) {
        // No-op
    }
}
