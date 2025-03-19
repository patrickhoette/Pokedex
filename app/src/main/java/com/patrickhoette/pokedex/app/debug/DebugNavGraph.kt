package com.patrickhoette.pokedex.app.debug

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.patrickhoette.pokedex.app.debug.DebugNavGraph.Playground
import com.patrickhoette.pokedex.app.debug.playground.PlaygroundScreen
import kotlinx.serialization.Serializable

@Serializable
object DebugNavGraph {

    @Serializable
    object Landing

    @Serializable
    object Playground
}

@Stable
fun NavGraphBuilder.debugRoutes(navController: NavHostController) = navigation<DebugNavGraph>(
    startDestination = DebugNavGraph.Landing,
) {
    composable<DebugNavGraph.Landing> {
        DebugScreen(
            onOpenShowcase = { navController.navigate(ShowcaseNavGraph) },
            onOpenPlayground = { navController.navigate(Playground) },
            modifier = Modifier.fillMaxSize(),
        )
    }

    composable<Playground> { PlaygroundScreen(Modifier.fillMaxSize()) }

    showcaseRoutes(navController)
}
