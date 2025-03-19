package com.patrickhoette.pokedex.app.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.patrickhoette.pokedex.app.debug.DebugNavHost
import com.patrickhoette.pokedex.app.main.MainNavGraph.Debug
import com.patrickhoette.pokedex.app.main.MainNavGraph.Pokemon
import com.patrickhoette.pokedex.app.pokemon.PokemonNavHost
import kotlinx.serialization.Serializable

@Serializable
object MainNavGraph {

    @Serializable
    object Debug

    @Serializable
    object Pokemon
}

@Stable
fun NavGraphBuilder.mainRoutes() = navigation<MainNavGraph>(
    startDestination = Pokemon,
) {
    composable<Pokemon> { PokemonNavHost(Modifier.fillMaxSize()) }

    composable<Debug> { DebugNavHost(Modifier.fillMaxSize()) }
}
