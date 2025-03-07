package com.patrickhoette.core.source.client

import io.github.aakira.napier.Napier
import io.ktor.client.plugins.logging.Logger
import org.koin.core.annotation.Factory

@Factory
class KtorLogger : Logger {

    override fun log(message: String) = Napier.i(message)
}
