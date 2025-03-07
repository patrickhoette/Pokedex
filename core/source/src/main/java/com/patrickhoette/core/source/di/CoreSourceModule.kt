package com.patrickhoette.core.source.di

import com.patrickhoette.core.source.client.KtorLogger
import com.patrickhoette.pokedex.entity.project.BuildOptions
import io.ktor.client.plugins.logging.Logger
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.patrickhoette.core.source")
class CoreSourceModule {

    @Single(binds = [Logger::class])
    fun provideKtorLogger() = KtorLogger()

    @Factory
    fun provideJson(buildOptions: BuildOptions) = Json {
        prettyPrint = buildOptions.isDebug
        isLenient = true
        ignoreUnknownKeys = true
        explicitNulls = false
        encodeDefaults = true
    }
}
