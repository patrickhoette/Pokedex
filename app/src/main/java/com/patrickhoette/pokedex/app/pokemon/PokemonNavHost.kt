package com.patrickhoette.pokedex.app.pokemon

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.patrickhoette.core.ui.animation.AnimationDefaults.DefaultEnterAnimation
import com.patrickhoette.core.ui.animation.AnimationDefaults.DefaultExitAnimation
import com.patrickhoette.core.ui.animation.AnimationDefaults.DefaultPopEnterAnimation
import com.patrickhoette.core.ui.animation.AnimationDefaults.DefaultPopExitAnimation

@Composable
fun PokemonNavHost(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = PokemonNavGraph,
        modifier = modifier,
        enterTransition = { DefaultEnterAnimation },
        exitTransition = { DefaultExitAnimation },
        popEnterTransition = { DefaultPopEnterAnimation },
        popExitTransition = { DefaultPopExitAnimation },
    ) {
        pokemonRoutes(navController)
    }
}
