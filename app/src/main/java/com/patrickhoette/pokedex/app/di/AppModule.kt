package com.patrickhoette.pokedex.app.di

import com.patrickhoette.core.data.di.CoreDataModule
import com.patrickhoette.core.domain.di.CoreDomainModule
import com.patrickhoette.core.presentation.di.CorePresentationModule
import com.patrickhoette.core.source.di.CoreSourceModule
import com.patrickhoette.core.store.di.CoreStoreModule
import com.patrickhoette.core.ui.di.CoreUIModule
import com.patrickhoette.pokedex.entity.di.EntityModule
import com.patrickhoette.pokemon.data.di.PokemonDataModule
import com.patrickhoette.pokemon.domain.di.PokemonDomainModule
import com.patrickhoette.pokemon.presentation.di.PokemonPresentationModule
import com.patrickhoette.pokemon.source.di.PokemonSourceModule
import com.patrickhoette.pokemon.store.di.PokemonStoreModule
import com.patrickhoette.pokemon.ui.di.PokemonUIModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [
        CoreDataModule::class,
        CoreDomainModule::class,
        CorePresentationModule::class,
        CoreSourceModule::class,
        CoreStoreModule::class,
        CoreUIModule::class,
        EntityModule::class,
        PokemonDataModule::class,
        PokemonDomainModule::class,
        PokemonPresentationModule::class,
        PokemonSourceModule::class,
        PokemonStoreModule::class,
        PokemonUIModule::class,
    ]
)
@ComponentScan("com.patrickhoette.pokedex.app")
class AppModule
