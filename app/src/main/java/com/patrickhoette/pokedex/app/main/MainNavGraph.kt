package com.patrickhoette.pokedex.app.main

import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.patrickhoette.pokedex.app.debug.debugRoutes
import com.patrickhoette.pokedex.app.pokemon.PokemonNavGraph
import com.patrickhoette.pokedex.app.pokemon.pokemonRoutes
import kotlinx.serialization.Serializable

@Serializable
data object MainNavGraph

@Stable
fun NavGraphBuilder.mainRoutes(navController: NavController) = navigation<MainNavGraph>(
    startDestination = PokemonNavGraph,
) {
    pokemonRoutes(navController)
    debugRoutes(navController)
}
