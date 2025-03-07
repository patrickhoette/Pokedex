package com.patrickhoette.core.source.client

import com.patrickhoette.core.source.error.APIErrorMapper
import com.patrickhoette.pokedex.entity.project.BuildOptions
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel.ALL
import io.ktor.client.plugins.logging.LogLevel.NONE
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.userAgent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Factory
import kotlin.time.Duration.Companion.seconds

@Factory
class KtorClientBuilder(
    private val errorMapper: APIErrorMapper,
    private val buildOptions: BuildOptions,
    private val logger: Logger,
    private val json: Json,
) {

    fun createClient() = HttpClient(CIO) {
        expectSuccess = true

        defaultRequest {
            url("https://pokeapi.co/api/v2/")
            contentType(ContentType.Application.Json)
            userAgent("Patrick Hoette; ${buildOptions.projectName}/${buildOptions.versionName}")
        }

        HttpResponseValidator {
            handleResponseException { cause, _ -> errorMapper.mapError(cause) }
        }

        install(HttpTimeout) {
            connectTimeoutMillis = 20.seconds.inWholeMilliseconds
            requestTimeoutMillis = 20.seconds.inWholeMilliseconds
            socketTimeoutMillis = 20.seconds.inWholeMilliseconds
        }

        install(Logging) {
            logger = this@KtorClientBuilder.logger
            level = if (buildOptions.isDebug) ALL else NONE
        }

        install(ContentNegotiation) {
            json(json)
        }
    }
}
