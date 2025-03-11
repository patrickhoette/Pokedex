package com.patrickhoette.pokedex.app.di

import com.patrickhoette.core.data.di.CoreDataModule
import com.patrickhoette.core.domain.di.CoreDomainModule
import com.patrickhoette.core.presentation.di.CorePresentationModule
import com.patrickhoette.core.source.di.CoreSourceModule
import com.patrickhoette.core.store.di.CoreStoreModule
import com.patrickhoette.core.ui.di.CoreUIModule
import com.patrickhoette.core.utils.coroutine.DispatcherProvider
import com.patrickhoette.pokedex.app.BuildConfig
import com.patrickhoette.pokedex.app.coroutine.DefaultDispatcherProvider
import com.patrickhoette.pokedex.entity.project.BuildOptions
import com.patrickhoette.pokemon.data.di.PokemonDataModule
import com.patrickhoette.pokemon.domain.di.PokemonDomainModule
import com.patrickhoette.pokemon.presentation.di.PokemonPresentationModule
import com.patrickhoette.pokemon.source.di.PokemonSourceModule
import com.patrickhoette.pokemon.store.di.PokemonStoreModule
import com.patrickhoette.pokemon.ui.di.PokemonUIModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(
    includes = [
        CoreDataModule::class,
        CoreDomainModule::class,
        CorePresentationModule::class,
        CoreSourceModule::class,
        CoreStoreModule::class,
        CoreUIModule::class,
        PokemonDataModule::class,
        PokemonDomainModule::class,
        PokemonPresentationModule::class,
        PokemonSourceModule::class,
        PokemonStoreModule::class,
        PokemonUIModule::class,
    ]
)
@ComponentScan("com.patrickhoette.pokedex.app")
class AppModule {

    @Single
    fun provideBuildOptions() = BuildOptions(
        versionName = BuildConfig.VERSION_NAME,
        projectName = BuildConfig.ProjectName,
        isDebug = BuildConfig.DEBUG,
    )

    @Factory(binds = [DispatcherProvider::class])
    fun provideDispatcherProvider() = DefaultDispatcherProvider
}
