package com.patrickhoette.core.source.client

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIOEngineConfig

interface InspectionHandler {

    fun installInspection(config: HttpClientConfig<CIOEngineConfig>)
}
