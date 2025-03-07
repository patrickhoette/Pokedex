@file:OptIn(KoinExperimentalAPI::class)

package com.patrickhoette.pokedex.app

import android.app.Application
import com.patrickhoette.pokedex.app.di.AppModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.koinConfiguration
import org.koin.ksp.generated.module

class PokedexApplication : Application(), KoinStartup {

    override fun onKoinStartup(): KoinConfiguration = koinConfiguration {
        if (BuildConfig.DEBUG) androidLogger()
        androidContext(this@PokedexApplication)
        modules(AppModule().module)
    }

    override fun onCreate() {
        super.onCreate()
        setupNapier()
    }

    private fun setupNapier() {
        if (BuildConfig.DEBUG) Napier.base(DebugAntilog())
    }
}
