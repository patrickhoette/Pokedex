package com.patrickhoette.pokedex.app.pokemon

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.patrickhoette.pokedex.app.pokemon.PokemonNavGraph.PokemonList
import com.patrickhoette.pokemon.ui.list.PokemonListScreen
import kotlinx.serialization.Serializable

@Serializable
object PokemonNavGraph {

    @Serializable
    object PokemonList
}

@Stable
fun NavGraphBuilder.pokemonRoutes(navController: NavHostController) = navigation<PokemonNavGraph>(
    startDestination = PokemonList,
) {
    composable<PokemonList> { PokemonListScreen(modifier = Modifier.fillMaxSize()) }
}

