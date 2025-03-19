package com.patrickhoette.pokedex.app.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
object FullscreenNavGraph {

    @Serializable
    object Main
}

@Stable
fun NavGraphBuilder.fullscreenRoutes() = navigation<FullscreenNavGraph>(
    startDestination = FullscreenNavGraph.Main,
) {
    composable<FullscreenNavGraph.Main> { MainScreen(Modifier.fillMaxSize()) }
}
